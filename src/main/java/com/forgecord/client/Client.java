package main.java.com.forgecord.client;

import main.java.com.forgecord.client.events.EventEmitter;
import main.java.com.forgecord.client.rest.RESTManager;
import main.java.com.forgecord.client.websocket.WebSocketManager;
import main.java.com.forgecord.structures.User;

public class Client extends EventEmitter{

	public User user;
	
	
	public String token;
	public WebSocketManager ws;
	public RESTManager rest;
	public ClientManager manager;
	
	public Client() {
		this.ws = new WebSocketManager(this);
		this.rest = new RESTManager(this);
		this.manager = new ClientManager(this);
	}
	
	public void login(String token) {
		this.rest.methods.login(token);
	}
	
}
