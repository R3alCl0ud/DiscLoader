package main.java.com.forgecord.client.websocket.packets.handlers;

import org.json.JSONObject;

import main.java.com.forgecord.client.websocket.packets.WebSocketPacketManager;

public class AbstractHandler {
	public WebSocketPacketManager packetManager;
	public AbstractHandler(WebSocketPacketManager packetManager) {
		
	}
	
	public JSONObject handle(JSONObject packet) {
		return packet;
	}
}
