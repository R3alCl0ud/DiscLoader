package io.disc.DiscLoader.socket;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class DiscSocket {

	/**
	 * 
	 */
	public DiscLoader loader;

	/**
	 * 
	 */
	public WebSocket ws;

	public int s;

	private TimerTask heartbeatInterval;

	public Timer timer = new Timer();

	public boolean lastHeartbeatAck;

	public Gson gson = new Gson();

	private DiscSocketListener socketListener;

	/**
	 * @param loader
	 */
	public DiscSocket(DiscLoader loader) {
		this.loader = loader;

		this.socketListener = new DiscSocketListener(this);
	}

	/**
	 * 
	 */
	public void connectSocket(String gateway) {
		try {
			this.ws = new WebSocketFactory().setConnectionTimeout(15000).createSocket(gateway)
					.addHeader("Accept-Encoding", "gzip").connect();
			this.ws.addListener(this.socketListener);
		} catch (WebSocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void keepAlive(int interval) {
		this.heartbeatInterval = new TimerTask() {
			public void run() {
				sendHeartbeat(true);
			}
		};
		timer.scheduleAtFixedRate(this.heartbeatInterval, interval, interval);
	}

	public void sendHeartbeat(boolean normal) {
		if (normal && !this.lastHeartbeatAck) {
			this.ws.disconnect(1007);
			return;
		}
		JSONObject payload = new JSONObject();
		payload.put("op", constants.OPCodes.HEARTBEAT).put("d", this.s);
		this.send(payload, true);
	}

	public void send(JSONObject payload, boolean force) {
		this.ws.sendText(payload.toString());
	}
	
	public void send(JSONObject packet) {
		this.send(packet, false);
	}
}
