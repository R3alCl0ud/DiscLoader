package io.discloader.discloader.network.gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.OPCodes;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 */
public class DiscSocket {

	/**
	 * The current instance of DiscLoader
	 */
	public DiscLoader loader;

	private DiscSocketListener socketListener;

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

	private int remaining = 120;

	public Gson gson = new Gson();

	private ArrayList<Object> queue;

	private final Logger logger = new DLLogger("Gateway").getLogger();

	public DiscSocket(DiscLoader loader) {
		this.loader = loader;

		socketListener = new DiscSocketListener(this);

		status = Status.IDLE;

		queue = new ArrayList<>();
	}

	public void connectSocket(String gateway) throws WebSocketException, IOException {
		logger.info(String.format("Connecting use gateway: %s", gateway));
		ws = new WebSocketFactory().setConnectionTimeout(15000).createSocket(gateway).addHeader("Accept-Encoding", "gzip");
		ws.addListener(socketListener);
		ws.connect();
		resetRemaining = new Thread("RemaingResetter") {

			public void run() {
				while (ws.isOpen() && !resetRemaining.isInterrupted()) {
					remaining = 120;
					handleQueue();
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};
		resetRemaining.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		resetRemaining.setDaemon(true);
		resetRemaining.start();
	}

	public void handleQueue() {
		if (!ws.isOpen() || remaining == 0 || queue.isEmpty()) return;

		Object payload = queue.get(0);
		remaining--;
		if (payload instanceof JSONObject) {
			ws.sendText(payload.toString());
		} else {
			ws.sendText(DLUtil.gson.toJson(payload));
		}
		queue.remove(payload);
		handleQueue();
	}

	public void keepAlive(final int interval) {
		heartbeatThread = new Thread("HeartbeatThread") {

			@Override
			public void run() {

				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				while (ws.isOpen() && !heartbeatThread.isInterrupted()) {
					sendHeartbeat(true);
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		};
		heartbeatThread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		heartbeatThread.setDaemon(true);
		heartbeatThread.start();
	}

	public void killHeartbeat() {
		if (heartbeatThread != null) {
			heartbeatThread.interrupt();
			heartbeatThread = null;
		}
		if (resetRemaining != null) {
			resetRemaining.interrupt();
			resetRemaining = null;
		}
	}

	public void send(Object payload) {
		send(payload, false);
	}

	public void send(Object payload, boolean force) {
		if (force) {
			ws.sendText(DLUtil.gson.toJson(payload));
			return;
		}
		queue.add(payload);
		handleQueue();
	}

	public void send(JSONObject payload) {
		send(payload, false);
	}

	public void send(JSONObject payload, boolean force) {
		if (force) {
			ws.sendText(payload.toString());
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
		send(payload, true);
		lastHeartbeatAck = false;
	}

	public void startGuildSync() {

	}

	public void setReady() {
		status = Status.READY;
	}
}
