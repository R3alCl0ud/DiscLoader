package main.java.com.forgecord.client.websocket.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.neovisionaries.ws.client.*;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.websocket.WebSocketManager;
import main.java.com.forgecord.client.websocket.packets.handlers.*;
import main.java.com.forgecord.util.constants;

public class WebSocketPacketManager extends WebSocketAdapter implements WebSocketListener {
	public WebSocketManager ws;
	public Client client;
	public HashMap<String, AbstractHandler> handlers;
	public List<JSONObject> queue;
	public static String[] BeforeReady = {constants.WSEvents.READY, constants.WSEvents.GUILD_CREATE, constants.WSEvents.GUILD_DELETE, constants.WSEvents.GUILD_MEMBER_ADD, constants.WSEvents.GUILD_MEMBER_REMOVE, constants.WSEvents.GUILD_MEMBERS_CHUNK};
	
	public WebSocketPacketManager(WebSocketManager webSocketManager) {

		this.ws = webSocketManager;
		
		this.client = this.ws.client;
		
		this.queue = new ArrayList<JSONObject>();
		
		this.handlers = new HashMap<String, AbstractHandler>();
		
		this.register(constants.WSEvents.READY, new Ready(this));
		this.register(constants.WSEvents.GUILD_CREATE, new GuildCreate(this));
	}
	
	public void register(String event, AbstractHandler  handler) {
		this.handlers.put(event, handler);
	}
	
	public void handleQueue() {
		this.queue.forEach(packet -> {
			this.handle(packet);
			this.queue.remove(packet);
		});
	}
	
	public void handleMessages() {
		this.ws.ws.addListener(this);
	}

	@Override
	public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

		
	}

	@Override
	public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
		this.client.emit("debug", "Sucessfully connected to gateway");
		this.ws.lastHeartbeatAck = true;
		this.sendNewIdentify();
	}

	@Override
	public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

		
	}

	@Override
	public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

		
	}

	@Override
	public void onTextMessage(WebSocket websocket, String text) throws Exception {
		this.client.emit("raw", text);
		JSONObject data = this.parsePacketData(text);
		
		if ((int) data.get("op") == 10) this.client.manager.setupKeepAlive(((JSONObject) data.get("d")).getInt("heartbeat_interval"));
		
		this.handle(data);
	}

	@Override
	public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

		
	}

	@Override
	public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

		
	}

	public void setSequence(int s) {
		if (s > this.ws.sequence) this.ws.sequence = s;
	}
	
	public void handle(JSONObject packet) {
		if (packet.getInt("op") == constants.OPCodes.RECONNECT) {
			this.setSequence(packet.getInt("s"));
			return;
		}
		
		if (packet.getInt("op") == constants.OPCodes.HEARTBEAT_ACK) {
			this.ws.lastHeartbeatAck = true;
			this.ws.client.emit("debug", "Heartbeat Acknoledged");
		} else if (packet.getInt("op") == constants.OPCodes.HEARTBEAT) {
			JSONObject payload = new JSONObject().put("op", constants.OPCodes.HEARTBEAT).put("d", this.client.ws.sequence);
			this.client.ws.send(payload);
			this.ws.client.emit("debug", "Received gateway heartbeat");
		}
		
		this.setSequence(packet.getInt("s"));
		
		if (this.handlers.containsKey(packet.get("t"))) {
			this.handlers.get(packet.get("t")).handle(packet);
			return;
		}
		
 	}
	
	public JSONObject parsePacketData(String packet) {
		return new JSONObject(packet);
	}

	public void sendNewIdentify() {
		this.ws.reconnecting = false;
		JSONObject payload = new JSONObject();
		JSONObject properties = new JSONObject().put("$os", "Forgecord").put("$browser", "Forgecord").put("$device", "Forgecord");
		payload.put("token", this.client.token).put("large_threshold", 250).put("compress", false).put("properties", properties);
		
		JSONObject packet = new JSONObject();
		packet.put("op", 2).put("d", payload);
		this.ws.send(packet);
		this.ws.sequence = -1;
	}
	
}
