package client.websocket;

import client.Client;
import client.websocket.packets.WebSocketPacketManager;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketManager {
	public Client client;
	public WebSocketPacketManager packetManager;
	
	public WebSocketManager(Client client) {
		this.client = client;
		this.packetManager = new WebSocketPacketManager(this);
	}
	
	public void _connect() {
		
	}
}
