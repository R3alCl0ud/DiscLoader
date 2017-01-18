package client;

import java.net.*;
import javafx.event.*;
import client.rest.RestManager;
import client.websocket.WebSocketManager;

public class Client {
	
	public WebSocketManager ws;
	public RestManager rest;
	
	public Client() {
		this.ws = new WebSocketManager(this);
	}
	
	public void login(String token) {
		
	}
	
	static class Ready extends Event {
		
		public static final EventType<Ready> ReadyEvent = new EventType<>(Event.ANY, "ReadyEvent");
		
		public Ready() {
			super(ReadyEvent);
		}
	}
	
}
