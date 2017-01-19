package main.java.com.forgecord.client.websocket.packets.handlers;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.websocket.packets.WebSocketPacketManager;

public class Ready extends AbstractHandler {

	public Ready(WebSocketPacketManager packetManager) {
		super(packetManager);
	}
	
	public JSONObject handle(JSONObject packet) {
		Client client = this.packetManager.ws.client;
		JSONObject data = (JSONObject) packet.get("d");
	
		client.ws.heartbeat(false);
		
		return null;
	}
	

}
