package io.disc.DiscLoader;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.disc.DiscLoader.events.DiscHandler;
import io.disc.DiscLoader.objects.gateway.GuildGateway;
import io.disc.DiscLoader.objects.loader.Mod;
import io.disc.DiscLoader.objects.loader.ModHandler;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.User;
import io.disc.DiscLoader.rest.DiscREST;
import io.disc.DiscLoader.socket.DiscSocket;
import io.disc.DiscLoader.util.constants;

/**
 * @author Perry Berman, Zachary Waldron
 * 
 */
public class DiscLoader {

	public DiscSocket discSocket;
	public String token;

	public DiscHandler handler;

	public DiscREST rest;
	
	public ModHandler modh;

	public HashMap<String, User> users;
	public HashMap<String, Channel> channels;
	public HashMap<String, Guild> guilds;
	public HashMap<String, Mod> mods;
	
	public User user;

	public DiscLoader() {
		this.discSocket = new DiscSocket(this);
		this.handler = new DiscHandler(this);
		this.rest = new DiscREST(this);
		this.handler.loadEvents();

		this.users = new HashMap<String, User>();

		this.channels = new HashMap<String, Channel>();
		this.guilds = new HashMap<String, Guild>();
		this.mods = new HashMap<String, Mod>();
	}

	public CompletableFuture<String> login(String token) {
		this.token = token;
		this.modh.beginLoader(null);
		CompletableFuture<String> future = this.rest.makeRequest(constants.Endpoints.gateway, constants.Methods.GET,
				true);
		future.thenAcceptAsync(text -> {
			System.out.println(text);
			Gson gson = new Gson();
			Gateway gateway = (Gateway) gson.fromJson(text, Gateway.class);
			this.discSocket.connectSocket(gateway.url + "?v=6&encoding=json");
		});
		return future;
	}

	public class Gateway {
		public String url;
	}

	public void emit(String event, Object data) {
		this.handler.emit(event, data);
	}

	public void emit(String event) {
		this.emit(event, null);
	}
	
	public Guild addGuild(GuildGateway guild) {
		boolean exists = this.guilds.containsKey(guild.id);
		
		Guild newGuild = new Guild(this, guild);
		this.guilds.put(newGuild.id, newGuild);
		if (!exists && this.discSocket.status == constants.Status.READY) {
			this.emit(constants.Events.GUILD_CREATE, newGuild);
		}
		return newGuild;
	}
	
}
