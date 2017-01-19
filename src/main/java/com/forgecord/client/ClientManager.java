package main.java.com.forgecord.client;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

public class ClientManager {
	
	public Client client;
	public TimerTask heartbeatInterval;
	public Timer timer = new Timer();
	
	public ClientManager(Client client) {
		this.client = client;
	}
	
	public void connect(Object value) {
		JSONObject response = new JSONObject(value.toString());
		String gateway = (String) response.get("url");
		try {
			this.client.ws.connect(gateway + "?v=6");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void failedLogin(UnirestException e) {
		e.printStackTrace();
	}
	
	@SuppressWarnings("unchecked")
	public void connectToWebSocket(String token) {
		this.client.token = token;
		this.client.rest.methods.getGateway().thenAccept(this::connect);
//		System.out.println("Test");
	}

	public void setupKeepAlive(int interval) {
		this.heartbeatInterval = new TimerTask() {
			@Override
			public void run() {
				client.ws.heartbeat(true);
				client.emit("debug", "Sending Heartbeat");
			}
		};
		timer.scheduleAtFixedRate(this.heartbeatInterval, interval, interval);
	}
}
