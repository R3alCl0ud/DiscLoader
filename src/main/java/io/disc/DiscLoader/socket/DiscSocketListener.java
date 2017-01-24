package io.disc.DiscLoader.socket;

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

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.socket.packets.Hello;
import io.disc.DiscLoader.socket.packets.SocketPacket;
import io.disc.DiscLoader.util.constants;

public class DiscSocketListener extends WebSocketAdapter implements WebSocketListener {
	public Gson gson = new Gson();

	public DiscLoader loader;
	public DiscSocket socket;

	public DiscSocketListener(DiscSocket socket) {
		this.socket = socket;
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
		System.out.println(text);
		System.out.println("test");
		SocketPacket packet = gson.fromJson(text, SocketPacket.class);
		if (packet.op == constants.OPCodes.HELLO) {
			packet.d = this.gson.fromJson(packet.d.toString(), Hello.class);
			this.socket.keepAlive(((Hello)packet.d).heartbeat_interval);
		}
	}

	public void onTextMessageError(WebSocket arg0, WebSocketException arg1, byte[] arg2) throws Exception {
		
	}

	public void onUnexpectedError(WebSocket arg0, WebSocketException arg1) throws Exception {
		
	}
	
	public void sendNewIdentify() {
		JSONObject payload = new JSONObject();
		JSONObject properties = new JSONObject().put("$os", "DiscLoader").put("$browser", "DiscLoader").put("$device", "DiscLoader");
		payload.put("token", this.socket.loader.token).put("large_threshold", 250).put("compress", false).put("properties", properties);
		
		JSONObject packet = new JSONObject();
		packet.put("op", 2).put("d", payload);
		this.socket.send(packet);
		this.socket.s = -1;
	}

}
