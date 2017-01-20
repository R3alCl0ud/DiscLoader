package main.java.com.forgecord.client;

import java.util.Date;
import java.util.HashMap;

import main.java.com.forgecord.client.events.EventEmitter;
import main.java.com.forgecord.client.rest.RESTManager;
import main.java.com.forgecord.client.websocket.WebSocketManager;
import main.java.com.forgecord.structures.Channel;
import main.java.com.forgecord.structures.ClientUser;
import main.java.com.forgecord.structures.Guild;
import main.java.com.forgecord.structures.User;

public class Client extends EventEmitter {

	public ClientUser user;
	public ClientManager manager;
	public ClientDataManager dataManager;
	public RESTManager rest;
	public WebSocketManager ws;
	
	public String token;

	public Date readyAt;
	
	public HashMap<String, Guild> guilds;
	public HashMap<String, Channel> channels;
	public HashMap<String, User> users;
	
	public Client() {
		this.ws = new WebSocketManager(this);
		this.rest = new RESTManager(this);
		this.manager = new ClientManager(this);
		this.dataManager = new ClientDataManager(this);
		
		this.guilds = new HashMap<String, Guild>();
		
		
		this.channels = new HashMap<String, Channel>();
		
		
		this.users = new HashMap<String, User>();
	}
	
	public void login(String token) {
		this.rest.methods.login(token);
	}
	
}
