package client;

import java.net.*;
import javafx.event.*;
import client.rest.RESTManager;
import client.websocket.WebSocketManager;

public class Client {
	
	public WebSocketManager ws;
	public RESTManager rest;
	
	public Client() {
		this.ws = new WebSocketManager(this);
		this.rest = new RESTManager(this);
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
