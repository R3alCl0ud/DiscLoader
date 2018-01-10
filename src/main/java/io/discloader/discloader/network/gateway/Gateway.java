package io.discloader.discloader.network.gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceStateUpdate;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.OPCodes;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 */
public class Gateway {

	/**
	 * The current instance of DiscLoader
	 */
	public DiscLoader loader;

	private GatewayListener socketListener;

	public WebSocket ws;

	public String sessionID;

	public int s;

	public int status;

	public boolean lastHeartbeatAck;

	public boolean first = true;

	public boolean normalReady = false;

	public boolean reconnecting = false;

	private Thread heartbeatThread = null;

	private Thread resetRemaining = null;

	private int remaining = 115, interval = 0;

	private Gson gson = new GsonBuilder().serializeNulls().create();

	private ArrayList<Object> queue;

	private final Logger logger;

	private final String logname;

	public Gateway(DiscLoader loader) {
		this.loader = loader;
		logname = loader.shards > 1 ? "Gateway (Shard: #" + loader.shardid + ")" : "Gateway";
		logger = new DLLogger(logname).getLogger();
		socketListener = new GatewayListener(this);

		status = Status.IDLE;

		queue = new ArrayList<>();
	}

	public void connectSocket(String gateway) throws WebSocketException, IOException {
		logger.info(String.format("Connecting to Gateway with Endpoint: %s", gateway));
		ws = new WebSocketFactory().setConnectionTimeout(15000).createSocket(gateway).addHeader("Accept-Encoding", "gzip");
		ws.addListener(socketListener);
		ws.connect();
	}

	public void handleQueue() {
		if (!ws.isOpen() || remaining == 0 || queue.isEmpty())
			return;

		Object raw_payload = queue.get(0);
		// loader.em
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
		logger.info("Sending: " + payload);
		ws.sendText(payload);
		queue.remove(raw_payload);
		handleQueue();
	}

	public void keepAlive() {
		keepAlive(interval);
	}

	public void keepAlive(final int interval) {
		this.interval = interval;
		heartbeatThread = new Thread(logname + " - Heartbeat") {

			@Override
			public void run() {

				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					logger.warning(String.format("The thread: %s - Heartbeat, has been interrupted", logname));
				}

				while (ws.isOpen() && !this.isInterrupted()) {
					sendHeartbeat(true);
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						logger.warning(String.format("The thread: %s - Heartbeat, has been interrupted", logname));
					}
				}

			}
		};
		resetRemaining = new Thread(logname + " - Rate Limiter") {
			public void run() {
				while (ws.isOpen() && !this.isInterrupted()) {
					remaining = 115;
					handleQueue();
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						logger.warning(String.format("The thread: %s - Rate Limiter, has been interrupted", logname));
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
			logger.info("Sending: " + payload.toString());
			ws.sendText(payload.toString());
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
			if (payload instanceof Packet && ((Packet) payload).d instanceof VoiceStateUpdate) {
				logger.info("Sending: " + gson.toJson(payload));
				ws.sendText(gson.toJson(payload));
			} else {
				logger.info("Sending: " + DLUtil.gson.toJson(payload));
				ws.sendText(DLUtil.gson.toJson(payload));
			}
			return;
		}
		queue.add(payload);
		handleQueue();
	}

	public void sendHeartbeat(boolean normal) {
		if (normal && !lastHeartbeatAck) {
			ws.disconnect(1007);
			return;
		}
		loader.emit("debug", "Attempting to Heartbeat");
		logger.info("Attempting to Heartbeat");
		JSONObject payload = new JSONObject();
		payload.put("op", OPCodes.HEARTBEAT).put("d", s);
		lastHeartbeatAck = false;
		send(payload, true);
	}

	public void setReady() {
		status = Status.READY;
	}

	public void setRetries(int i) {
		socketListener.setRetries(i);
	}

	public void startGuildSync() {

	}
}
