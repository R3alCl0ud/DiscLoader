package main.java.com.forgecord.client.websocket;

import java.io.*;

import org.json.JSONObject;

import com.neovisionaries.ws.client.*;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.websocket.packets.WebSocketPacketManager;
import main.java.com.forgecord.util.constants;

public class WebSocketManager {
	public Client client;
	public WebSocketPacketManager packetManager;
	public WebSocket ws;
	
	public String sessionID;
	public String gateway;

	public int sequence;
	public int status;
	
	public boolean normalReady = false;
	public boolean first = true;
	public boolean lastHeartbeatAck = false;
	public boolean reconnecting = false;
	
	public WebSocketManager(Client client) {
		this.client = client;
		
		this.status = constants.Status.IDLE;
		
		this.packetManager = new WebSocketPacketManager(this);
	}
	
	public void _connect(String gateway) throws IOException, WebSocketException {
		System.out.printf("Connecting to gateway %s%n", gateway);
		this.ws = new WebSocketFactory().setConnectionTimeout(15000).createSocket(gateway).addHeader("Accept-Encoding", "gzip").connect();
		this.packetManager.handleMessages();
	}
	
	public void connect(String gateway) throws Exception {
		gateway += "&encoding=json";
		if (this.first) {
			this._connect(gateway);
			this.first = false;
		} else {
			Thread.sleep(5500);
			this._connect(gateway);
		}
	}
	
	public void heartbeat(boolean normal) {
		if (normal && !this.lastHeartbeatAck) {
			this.ws.disconnect(1007);
			return;
		}
		
		this.client.emit("debug", "Sending Heartbeat");
		JSONObject payload = new JSONObject();
		payload.put("op", constants.OPCodes.HEARTBEAT).put("d", this.sequence);
		this.send(payload, true);
	}

	public void send(JSONObject payload, boolean force) {
		this.ws.sendText(payload.toString());
	}
	
	public void send(JSONObject payload) {
		System.out.println(payload);
		this.ws.sendText(payload.toString());
	}
}
