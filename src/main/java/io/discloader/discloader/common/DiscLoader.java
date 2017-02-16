package io.discloader.discloader.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.discloader.discloader.common.registry.DiscRegistry;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.common.structures.Guild;
import io.discloader.discloader.common.structures.User;
import io.discloader.discloader.common.structures.channels.Channel;
import io.discloader.discloader.common.structures.channels.PrivateChannel;
import io.discloader.discloader.common.structures.channels.TextChannel;
import io.discloader.discloader.common.structures.channels.VoiceChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.ChannelJSON;
import io.discloader.discloader.network.gateway.json.GuildJSON;
import io.discloader.discloader.network.gateway.json.UserJSON;
import io.discloader.discloader.network.rest.RESTManager;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman, Zachary Waldron
 */
public class DiscLoader {

	public DiscSocket discSocket;
	
	public String token;

	public boolean ready;

	public DiscRegistry registry;
	
	public RESTManager rest;

	public ModRegistry modh;

	/**
	 * A HashMap of the client's cached users. Indexed by {@link User#id}.
	 * @author Perry Berman
	 * @see User
	 * @see HashMap
	 */
	public HashMap<String, User> users;
	
	/**
	 * A HashMap of the client's cached channels. Indexed by {@link Channel#id}.
	 * @author Perry Berman
	 * @see Channel
	 * @see HashMap
	 */
	public HashMap<String, Channel> channels;
	
	/**
	 * A HashMap of the client's cached PrivateChannels. Indexed by {@link Channel#id}.
	 * @see Channel
	 * @see PrivateChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, PrivateChannel> privateChannels;
	
	/**
	 * A HashMap of the client's cached TextChannels. Indexed by {@link Channel#id}.
	 * @see Channel
	 * @see TextChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, TextChannel> textChannels;
	
	/**
	 * A HashMap of the client's cached VoiceChannels. Indexed by {@link Channel#id}.
	 * @see Channel
	 * @see VoiceChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, VoiceChannel> voiceChannels;
	
	/**
	 * A HashMap of the client's cached Guilds. Indexed by {@link Guild#id}
	 * @see Guild
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, Guild> guilds;

	/**
	 * The User we are currently logged in as.
	 */
	public User user;

	public Timer timer;

	public DiscLoader() {
		this.discSocket = new DiscSocket(this);
		
		this.rest = new RESTManager(this);

		this.registry = new DiscRegistry(this);
		
		this.users = new HashMap<String, User>();

		this.channels = new HashMap<String, Channel>();
		
		this.privateChannels = new HashMap<String, PrivateChannel>();
		
		this.textChannels = new HashMap<String, TextChannel>();
		
		this.voiceChannels = new HashMap<String, VoiceChannel>();
		
		this.guilds = new HashMap<String, Guild>();

		this.modh = new ModRegistry(this);

		this.timer = new Timer();

		this.ready = false;
	}

	/**
	 * Connects the current instance of the {@link DiscLoader loader} into Discord's gateway servers 
	 * @param token your API token
	 * @return {@literal CompletableFuture<String>}
	 */
	public CompletableFuture<String> login(String token) {
		this.token = token;
		// this.modh.beginLoader();
		CompletableFuture<String> future = this.rest.makeRequest(Constants.Endpoints.gateway, Constants.Methods.GET,
				true);
		future.thenAcceptAsync(text -> {
			System.out.println(text);
			Gson gson = new Gson();
			Gateway gateway = gson.fromJson(text, Gateway.class);
			try {
				this.discSocket.connectSocket(gateway.url + "?v=6&encoding=json");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return future;
	}

	public class Gateway {
		public String url;
	}

	/**
	 * 
	 * @param event
	 * @param data
	 */
	public void emit(String event, Object data) {
//		this.modh.emit(event, data);
	}

	/**
	 * @param event
	 */
	public void emit(String event) {
		this.emit(event, null);
	}

	public Guild addGuild(GuildJSON guild) {
		boolean exists = this.guilds.containsKey(guild.id);

		Guild newGuild = new Guild(this, guild);
		this.guilds.put(newGuild.id, newGuild);
		if (!exists && this.discSocket.status == Constants.Status.READY) {
			this.emit(Constants.Events.GUILD_CREATE, newGuild);
		}
		return newGuild;
	}

	public Channel addChannel(ChannelJSON data) {
		return this.addChannel(data, null);
	}

	public Channel addChannel(ChannelJSON data, Guild guild) {
		boolean exists = this.channels.containsKey(data.id);
		Channel channel = null;
		if (data.type == Constants.ChannelTypes.DM) {
			channel = new PrivateChannel(this, data);
		} else if (data.type == Constants.ChannelTypes.groupDM) {
			channel = new Channel(this, data);
		} else {
			if (guild != null) {
				if (data.type == Constants.ChannelTypes.text) {
					channel = new TextChannel(guild, data);
					guild.textChannels.put(channel.id, (TextChannel) channel);
				} else if (data.type == Constants.ChannelTypes.voice) {
					channel = new VoiceChannel(guild, data);
					guild.voiceChannels.put(channel.id, (VoiceChannel) channel);
				}
			}
		}

		if (channel != null) {
			switch (channel.type) {
			case "text":
				this.textChannels.put(channel.id, (TextChannel) channel);
				break;
			case "dm":
				// this.textChannels.put(channel.id, (TextChannel) channel);
				this.privateChannels.put(channel.id, (PrivateChannel) channel);
				break;
			case "voice":
				this.voiceChannels.put(channel.id, (VoiceChannel) channel);
				break;
			default:
				this.channels.put(channel.id, channel);
			}
			this.channels.put(channel.id, channel);
			if (!exists && this.ready) {
				this.emit(Constants.Events.CHANNEL_CREATE, channel);
			}
			return channel;
		}

		return null;
	}

	public User addUser(UserJSON data) {
		if (this.users.containsKey(data.id))
			return this.users.get(data.id);
		User user = new User(this, data);
		this.users.put(user.id, user);
		return user;
	}

	public void checkReady() {
		if (this.discSocket.status != Constants.Status.READY && this.discSocket.status != Constants.Status.NEARLY) {
			int unavailable = 0;
			Collection<Guild> guilds = this.guilds.values();
			for (Guild guild : guilds) {
				unavailable += guild.available ? 0 : 1;
			}
			if (unavailable == 0) {
				this.discSocket.status = Constants.Status.NEARLY;
				this.emitReady();
			}
		}
	}

	public void emitReady() {
		this.discSocket.status = Constants.Status.READY;
		this.ready = true;
		this.emit(Constants.Events.READY, this);
	}

}
