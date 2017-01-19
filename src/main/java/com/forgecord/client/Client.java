package main.java.com.forgecord.client;

import java.util.HashMap;

import main.java.com.forgecord.client.events.EventEmitter;
import main.java.com.forgecord.client.rest.RESTManager;
import main.java.com.forgecord.client.websocket.WebSocketManager;
import main.java.com.forgecord.structures.Channel;
import main.java.com.forgecord.structures.ClientUser;
import main.java.com.forgecord.structures.Guild;
import main.java.com.forgecord.structures.User;

public class Client extends EventEmitter{

	public ClientUser user;
	
	public HashMap<String, Guild> guilds;
	public HashMap<String, ? extends Channel> channels;
	public HashMap<String, User> users;
	
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
