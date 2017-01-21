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
 * The Client
 * @author Perry Berman
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

	public Client() {
		this.ws = new WebSocketManager(this);
		this.rest = new RESTManager(this);
		this.manager = new ClientManager(this);
		this.dataManager = new ClientDataManager(this);

		this.guilds = new HashMap<String, Guild>();

		this.channels = new HashMap<String, Channel>();

		this.users = new HashMap<String, User>();
	}

	/**
	 * 
	 * @param token
	 */
	public final void login(String token) {
		this.rest.methods.login(token);
	}
	
	/**
	 * @param reason The reason for logging out
	 */
	public final void logout(String reason) {
		this.rest.methods.logout();
	}
	
	/**
	 * @param reason
	 */
	public final void disconnect(String reason) {
		
	}

}
