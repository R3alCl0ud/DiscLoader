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

/**
 * 
 * @author perryberman
 *
 */
public class Client extends EventEmitter {

	/**
	 * The currently logged in user/bot account
	 */
	public ClientUser user;
	public ClientManager manager;
	public ClientDataManager dataManager;
	public RESTManager rest;
	public WebSocketManager ws;

	public String token;

	/**
	 * The time at witch the {@link Client} became ready to communicate with the websocket
	 */
	public Date readyAt;

	/**
	 * The client's cached guilds
	 */
	public HashMap<String, Guild> guilds;

	/**
	 * The client's cached channels
	 */
	public HashMap<String, Channel> channels;

	/**
	 * The client's cached channels
	 */
	public HashMap<String, User> users;

	/**
	 * The main Client.
	 * @author perryberman
	 */
	public Client() {
		this.ws = new WebSocketManager(this);
		this.rest = new RESTManager(this);
		this.manager = new ClientManager(this);
		this.dataManager = new ClientDataManager(this);

		this.guilds = new HashMap<String, Guild>();

		this.channels = new HashMap<String, Channel>();

		this.users = new HashMap<String, User>();
	}

	public final void login(String token) {
		this.rest.methods.login(token);
	}
	
	public final void logout(String reason) {
		this.rest.methods.logout();
	}
	
	public final void disconnect(String reason) {
		
	}

}
