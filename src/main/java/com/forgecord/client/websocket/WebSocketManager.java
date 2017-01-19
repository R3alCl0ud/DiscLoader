package main.java.com.forgecord.client.websocket;

import java.io.*;
import com.neovisionaries.ws.client.*;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.websocket.packets.WebSocketPacketManager;

public class WebSocketManager {
	public Client client;
	public WebSocketPacketManager packetManager;
	public String sessionID;
	public int sequence;
	public String gateway;
	public boolean normalReady;
	public boolean first = true;
	public WebSocket ws;
	
	public WebSocketManager(Client client) {
		this.client = client;
		
		
		this.packetManager = new WebSocketPacketManager(this);
	}
	
	public void _connect(String gateway) throws IOException, WebSocketException {
		System.out.printf("Connecting to gateway %s", gateway);
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
}
