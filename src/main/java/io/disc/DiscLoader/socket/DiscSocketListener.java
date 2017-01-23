package io.disc.DiscLoader.socket;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import io.disc.DiscLoader.socket.packets.SocketPacket;

public class DiscSocketListener extends WebSocketAdapter implements WebSocketListener {
	public Gson gson = new Gson();
	
	
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

	public void onTextMessage(WebSocket ws, String text) throws Exception {
		System.out.println(text);
		SocketPacket packet = gson.fromJson(text, SocketPacket.class);
	}

	public void onTextMessageError(WebSocket arg0, WebSocketException arg1, byte[] arg2) throws Exception {
	}

	public void onUnexpectedError(WebSocket arg0, WebSocketException arg1) throws Exception {
	}

}
