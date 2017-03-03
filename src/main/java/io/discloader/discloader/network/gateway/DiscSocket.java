package io.discloader.discloader.network.gateway;

import java.io.IOException;
import java.util.Timer;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class DiscSocket {

	/**
	 * 
	 */
	public DiscLoader loader;

	private DiscSocketListener socketListener;
	/**
	 * 
	 */
	public WebSocket ws;

	public String sessionID;

	public int s;
	public int status;

	public boolean lastHeartbeatAck;
	public boolean first = true;
	public boolean normalReady = false;
	public boolean reconnecting = false;

	private Thread heartbeatThread = null;

	public Timer timer = new Timer();

	public Gson gson = new Gson();

	/**
	 * @param loader
	 */
	public DiscSocket(DiscLoader loader) {
		this.loader = loader;

		this.socketListener = new DiscSocketListener(this);

		this.status = Constants.Status.IDLE;
	}

	/**
	 * @throws IOException
	 * @throws WebSocketException
	 * 
	 */
	public void connectSocket(String gateway) throws WebSocketException, IOException {
		System.out.printf("Connecting use gateway: %s", gateway);
		this.ws = new WebSocketFactory().setConnectionTimeout(15000).createSocket(gateway).addHeader("Accept-Encoding",
				"gzip");
		this.ws.addListener(this.socketListener);
		this.ws.connect();
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
		}
	}

	public void sendHeartbeat(boolean normal) {
		if (normal && !this.lastHeartbeatAck) {
			this.ws.disconnect(1007);
			return;
		}
		this.loader.emit("debug", "Attempting heartbeat");
		JSONObject payload = new JSONObject();
		payload.put("op", Constants.OPCodes.HEARTBEAT).put("d", this.s);
		this.send(payload, true);
		this.lastHeartbeatAck = false;
	}

	public void send(Object payload, boolean force) {
		this.ws.sendText(Constants.gson.toJson(payload));
	}

	public void send(Object payload) {
		this.ws.sendText(Constants.gson.toJson(payload));
	}

	public void send(JSONObject payload, boolean force) {
		this.ws.sendText(payload.toString());
	}

	public void send(JSONObject packet) {
		this.send(packet, false);
	}
}
