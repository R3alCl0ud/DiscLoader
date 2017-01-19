package main.java.com.forgecord.client.websocket.packets;

import java.util.List;
import java.util.Map;

import com.neovisionaries.ws.client.*;

import main.java.com.forgecord.client.websocket.WebSocketManager;

public class WebSocketPacketManager extends WebSocketAdapter implements WebSocketListener {
	public WebSocketManager ws;
	
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

		
	}

	@Override
	public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

		
	}

	@Override
	public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

		
	}


}
