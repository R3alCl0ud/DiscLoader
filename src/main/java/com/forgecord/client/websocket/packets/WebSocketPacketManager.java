package main.java.com.forgecord.client.websocket.packets;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.neovisionaries.ws.client.*;

import main.java.com.forgecord.client.websocket.WebSocketManager;

public class WebSocketPacketManager extends WebSocketAdapter implements WebSocketListener {
	public WebSocketManager ws;
	public boolean lastHeartBeatAck = false;
	public boolean reconnecting = false;
	
	public WebSocketPacketManager(WebSocketManager webSocketManager) {

		this.ws = webSocketManager;
	}
	
	public void handleMessages() {
		this.ws.ws.addListener(this);
	}

	@Override
	public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

		
	}

	@Override
	public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
		System.out.println("Sucessfully connected to gateway");
		this.lastHeartBeatAck = true;
		this.sendNewIdentify();
	}

	@Override
	public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

		
	}

	@Override
	public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame,
			boolean closedByServer) throws Exception {

		
	}

	@Override
	public void onTextMessage(WebSocket websocket, String text) throws Exception {
		System.out.println(text);
		JSONObject data = this.parsePacketData(text);
	}

	@Override
	public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

		
	}

	@Override
	public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

		
	}

	public void handle(JSONObject packet) {
		if (packet.getInt("op") == 7) {
			
		}
 	}
	
	public JSONObject parsePacketData(String packet) {
		return new JSONObject(packet);
	}

	public void sendNewIdentify() {
		this.reconnecting = false;
		JSONObject payload = new JSONObject();
		JSONObject properties = new JSONObject().put("$os", "Forgecord").put("$browser", "Forgecord").put("$device", "Forgecord");
		payload.put("token", this.ws.client.token).put("large_threshold", 250).put("compress", false).put("properties", properties);
		
		JSONObject packet = new JSONObject();
		packet.put("op", 2).put("d", payload);
		this.ws.send(packet);
		this.ws.sequence = -1;
	}
	
}
