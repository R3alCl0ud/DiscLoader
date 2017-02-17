package io.discloader.discloader.network.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.gateway.packets.DiscPacket;
import io.discloader.discloader.network.gateway.packets.GuildCreate;
import io.discloader.discloader.network.gateway.packets.HelloPacket;
import io.discloader.discloader.network.gateway.packets.MessageCreate;
import io.discloader.discloader.network.gateway.packets.MessageDelete;
import io.discloader.discloader.network.gateway.packets.MessageUpdate;
import io.discloader.discloader.network.gateway.packets.PresenceUpdate;
import io.discloader.discloader.network.gateway.packets.ReadyPacket;
import io.discloader.discloader.network.gateway.packets.SocketPacket;
import io.discloader.discloader.util.Constants;

public class DiscSocketListener extends WebSocketAdapter implements WebSocketListener {
	public Gson gson = new Gson();

	public DiscLoader loader;
	public DiscSocket socket;

	public HashMap<String, DiscPacket> handlers;

	public List<SocketPacket> queue;

	public DiscSocketListener(DiscSocket socket) {
		this.socket = socket;
		this.handlers = new HashMap<String, DiscPacket>();
		this.queue = new ArrayList<SocketPacket>();

		this.register(Constants.WSEvents.HELLO, new HelloPacket(this.socket));
		this.register(Constants.WSEvents.READY, new ReadyPacket(this.socket));
		this.register(Constants.WSEvents.GUILD_CREATE, new GuildCreate(this.socket));
		this.register(Constants.WSEvents.PRESENCE_UPDATE, new PresenceUpdate(this.socket));
		this.register(Constants.WSEvents.MESSAGE_CREATE, new MessageCreate(this.socket));
		this.register(Constants.WSEvents.MESSAGE_UPDATE, new MessageUpdate(this.socket));
		this.register(Constants.WSEvents.MESSAGE_DELETE, new MessageDelete(this.socket));
	}

	public void setSequence(int s) {
		if (s > this.socket.s)
			this.socket.s = s;
	}

	public void register(String event, DiscPacket handler) {
		this.handlers.put(event, handler);
	}

	public void handleQueue() {
		this.queue.forEach(packet -> {
			this.handle(packet);
			this.queue.remove(packet);
		});
	}

	public void handle(SocketPacket packet) {
		if (packet.op == Constants.OPCodes.RECONNECT) {
			this.setSequence(packet.s);
			return;
		}

		if (packet.op == Constants.OPCodes.HELLO) {
			this.handlers.get(Constants.WSEvents.HELLO).handle(packet);
		}

		if (packet.op == Constants.OPCodes.HEARTBEAT_ACK) {
			this.socket.lastHeartbeatAck = true;
			this.loader.emit("debug", "Heartbeat Acknowledged");
		} else if (packet.op == Constants.OPCodes.HEARTBEAT) {
			JSONObject payload = new JSONObject().put("op", Constants.OPCodes.HEARTBEAT).put("d", this.socket.s);
			this.socket.send(payload);
			this.loader.emit("debug", "Recieved gateway heartbeat");
		}

		this.setSequence(packet.s);
		
		if (this.socket.status != Constants.Status.READY) {
			if (Constants.EventWhitelist.indexOf(packet.t) == -1) {
				this.queue.add(packet);
				return;
			}
		}

		if (packet.op == Constants.OPCodes.DISPATCH) {
			if (!this.handlers.containsKey(packet.t))
				return;
			this.handlers.get(packet.t).handle(packet);
		}
	}

	public void handleCallbackError(WebSocket ws, Throwable arg1) throws Exception {
	}

	public void onBinaryFrame(WebSocket ws, WebSocketFrame arg1) throws Exception {
	}

	public void onBinaryMessage(WebSocket ws, byte[] bytes) throws Exception {
	}

	public void onCloseFrame(WebSocket ws, WebSocketFrame frame) throws Exception {
	}

	public void onConnectError(WebSocket ws, WebSocketException e) throws Exception {
	}

	public void onConnected(WebSocket ws, Map<String, List<String>> arg1) throws Exception {
		System.out.println("connected to gateway");
		this.socket.lastHeartbeatAck = true;
		this.sendNewIdentify();
	}

	public void onContinuationFrame(WebSocket ws, WebSocketFrame frame) throws Exception {
	}

	public void onDisconnected(WebSocket ws, WebSocketFrame frame_1, WebSocketFrame frame_2, boolean isDisconnected)
			throws Exception {
		System.out.println("Got disconnected from the websocket");
	}

	public void onError(WebSocket ws, WebSocketException e) throws Exception {
	}

	public void onFrame(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	public void onFrameError(WebSocket arg0, WebSocketException arg1, WebSocketFrame arg2) throws Exception {
	}

	public void onFrameSent(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	public void onFrameUnsent(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	public void onMessageDecompressionError(WebSocket arg0, WebSocketException arg1, byte[] arg2) throws Exception {
	}

	public void onMessageError(WebSocket arg0, WebSocketException arg1, List<WebSocketFrame> arg2) throws Exception {
	}

	public void onPingFrame(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	public void onPongFrame(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	public void onSendError(WebSocket arg0, WebSocketException arg1, WebSocketFrame arg2) throws Exception {
	}

	public void onSendingFrame(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	public void onSendingHandshake(WebSocket arg0, String arg1, List<String[]> arg2) throws Exception {
	}

	public void onStateChanged(WebSocket arg0, WebSocketState arg1) throws Exception {
	}

	public void onTextFrame(WebSocket arg0, WebSocketFrame arg1) throws Exception {
	}

	@Override
	public void onTextMessage(WebSocket ws, String text) throws Exception {
		this.socket.loader.emit("raw", text);
		SocketPacket packet = gson.fromJson(text, SocketPacket.class);
		this.handle(packet);
	}

	public void onTextMessageError(WebSocket arg0, WebSocketException arg1, byte[] arg2) throws Exception {

	}

	public void onUnexpectedError(WebSocket arg0, WebSocketException arg1) throws Exception {

	}

	public void sendNewIdentify() {
		JSONObject payload = new JSONObject();
		JSONObject properties = new JSONObject().put("$os", "DiscLoader").put("$browser", "DiscLoader").put("$device",
				"DiscLoader");
		payload.put("token", this.socket.loader.token).put("large_threshold", 250).put("compress", false)
				.put("properties", properties);

		JSONObject packet = new JSONObject();
		packet.put("op", 2).put("d", payload);
		this.socket.send(packet);
		this.socket.s = -1;
	}

}
