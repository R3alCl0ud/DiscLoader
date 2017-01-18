package client.websocket.packets;

import client.websocket.WebSocketManager;

public class WebSocketPacketManager {
	public WebSocketManager ws;
	
	public WebSocketPacketManager(WebSocketManager webSocketManager) {
		this.ws = webSocketManager;
	}
}
