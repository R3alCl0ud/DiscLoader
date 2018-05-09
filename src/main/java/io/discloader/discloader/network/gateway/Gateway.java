package io.discloader.discloader.network.gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceStateUpdate;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.OPCodes;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 */
public class Gateway {

	private int remaining = 115, interval = 0;
	public boolean first = true, normalReady = false, reconnecting = false;
	public String sessionID, logName;

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;
	private GatewayListener gatewayListener;
	public WebSocket websocket;
	public AtomicInteger sequence = new AtomicInteger(), status = new AtomicInteger();
	public AtomicBoolean lastHeartbeatAck = new AtomicBoolean();
	private Thread heartbeatThread = null, resetRemaining = null;
	private Gson gson = new GsonBuilder().serializeNulls().create();
	private List<Object> queue;
	private final Logger logger;

	public Gateway(DiscLoader loader) {
		this.loader = loader;
		logName = loader.shards > 1 ? "Gateway (Shard: #" + loader.shardid + ")" : "Gateway";
		logger = DLLogger.getLogger(logName);
		gatewayListener = new GatewayListener(this);

		status.set(Status.IDLE);

		queue = new ArrayList<>();
	}

	public void connectSocket(String gateway) throws WebSocketException, IOException {
		if (loader.getOptions().isDebugging()) {
			logger.config(String.format("Connecting to Gateway with Endpoint: %s", gateway));
		}
		websocket = new WebSocketFactory().setConnectionTimeout(15000).createSocket(gateway).addHeader("Accept-Encoding", "gzip").addListener(gatewayListener).connect();
	}

	public void handleQueue() {
		if (!websocket.isOpen() || remaining == 0 || queue.isEmpty())
			return;

		Object raw_payload = queue.get(0);
		remaining--;
		String payload = "";
		if (raw_payload instanceof JSONObject) {
			payload = raw_payload.toString();
		} else {
			if (raw_payload instanceof Packet && ((Packet) raw_payload).d instanceof VoiceStateUpdate) {
				payload = gson.toJson(raw_payload);
			} else {
				payload = DLUtil.gson.toJson(raw_payload);
			}
		}
		if (loader.getOptions().isDebugging()) {
			logger.config("Sending: " + payload);
		}
		websocket.sendText(payload);
		queue.remove(raw_payload);
		handleQueue();
	}

	public void keepAlive() {
		keepAlive(interval);
	}

	public void keepAlive(final int interval) {
		this.interval = interval;
		heartbeatThread = new Thread(logName + " - Heartbeat") {

			@Override
			public void run() {
				try {
					Thread.sleep(interval);
					while (websocket.isOpen() && !this.isInterrupted()) {
						sendHeartbeat(true);
						Thread.sleep(interval);
					}
				} catch (InterruptedException e) {
					logger.warning(String.format("The thread: %s - Heartbeat, has been interrupted", logName));
				}

			}
		};
		resetRemaining = new Thread(logName + " - Rate Limiter") {
			public void run() {
				while (websocket.isOpen() && !this.isInterrupted()) {
					remaining = 115;
					handleQueue();
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						logger.warning(String.format("The thread: %s - Rate Limiter, has been interrupted", logName));
					}

				}
			}
		};
		resetRemaining.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		resetRemaining.setDaemon(true);
		resetRemaining.start();
		heartbeatThread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		heartbeatThread.setDaemon(true);
		heartbeatThread.start();
	}

	public void killHeartbeat() {
		logger.severe("Killing heartbeat");
		if (heartbeatThread != null) {
			heartbeatThread.interrupt();
			heartbeatThread = null;
		}
		if (resetRemaining != null) {
			resetRemaining.interrupt();
			resetRemaining = null;
		}
	}

	public void send(JSONObject payload) {
		send(payload, false);
	}

	public void send(JSONObject payload, boolean force) {
		if (force && remaining != 0) {
			remaining--;
			if (loader.getOptions().isDebugging()) {
				logger.config("Sending: " + payload.toString());
			}
			websocket.sendText(payload.toString());
			return;
		}
		queue.add(payload);
		handleQueue();
	}

	public void send(Object payload) {
		send(payload, false);
	}

	public void send(Object payload, boolean force) {
		if (force && remaining != 0) {
			remaining--;
			String payloadText = "";
			if (payload instanceof Packet && ((Packet) payload).d instanceof VoiceStateUpdate) {
				payloadText = gson.toJson(payload);
			} else {
				payloadText = DLUtil.gson.toJson(payload);
			}
			if (loader.getOptions().isDebugging()) {
				logger.config((String.format("Sending: %s", payloadText)));
			}
			websocket.sendText(payloadText);
			return;
		}
		queue.add(payload);
		handleQueue();
	}

	public void sendHeartbeat(boolean normal) {
		if (normal && !lastHeartbeatAck.get()) {
			logger.severe("Heartbeat Not Acknowledged");
			if (websocket.isOpen()) { // if the ws connection is still open close it.
				websocket.disconnect(1007, "Heartbeat Not Acknowledged");
			} else { // if the connection is already closed try to reconnect.
				gatewayListener.tryReconnecting();
			}
			return;
		}
		if (loader.getOptions().isDebugging()) {
			logger.config("Attempting to Heartbeat");
		}
		JSONObject payload = new JSONObject();
		payload.put("op", OPCodes.HEARTBEAT).put("d", sequence);
		lastHeartbeatAck.set(false);
		send(payload, true);
	}

	public void setReady() {
		setStatus(Status.READY);
	}

	public void setStatus(int status) {
		this.status.set(status);
	}

	public void setRetries(int i) {
		gatewayListener.setRetries(i);
	}
}
