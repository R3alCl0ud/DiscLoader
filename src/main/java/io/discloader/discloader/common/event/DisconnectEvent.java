package io.discloader.discloader.common.event;

import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.common.DiscLoader;

public class DisconnectEvent extends DLEvent {

	private WebSocketFrame serverFrame, clientFrame;
	private boolean isServer;

	public DisconnectEvent(DiscLoader loader, WebSocketFrame serverFrame, WebSocketFrame clientFrame, boolean isServer) {
		super(loader);
		this.serverFrame = serverFrame;
		this.clientFrame = clientFrame;
		this.isServer = isServer;
	}

	public WebSocketFrame getClientFrame() {
		return clientFrame;
	}

	public WebSocketFrame getServerFrame() {
		return serverFrame;
	}

	public boolean isServer() {
		return isServer;
	}
}
