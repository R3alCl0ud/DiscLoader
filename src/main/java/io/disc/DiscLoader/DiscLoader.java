package io.disc.DiscLoader;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.disc.DiscLoader.events.DiscHandler;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;
import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.objects.gateway.UserJSON;
import io.disc.DiscLoader.objects.loader.Mod;
import io.disc.DiscLoader.objects.loader.ModHandler;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildChannel;
import io.disc.DiscLoader.objects.structures.PrivateChannel;
import io.disc.DiscLoader.objects.structures.TextChannel;
import io.disc.DiscLoader.objects.structures.User;
import io.disc.DiscLoader.objects.structures.VoiceChannel;
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

	public boolean ready;

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

		this.modh = new ModHandler();

		this.ready = false;
	}

	public CompletableFuture<String> login(String token) {
		this.token = token;
		// this.modh.beginLoader();
		CompletableFuture<String> future = this.rest.makeRequest(constants.Endpoints.gateway, constants.Methods.GET,
				true);
		future.thenAcceptAsync(text -> {
			System.out.println(text);
			Gson gson = new Gson();
			Gateway gateway = (Gateway) gson.fromJson(text, Gateway.class);
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

	public void emit(String event, Object data) {
		this.handler.emit(event, data);
	}

	public void emit(String event) {
		this.emit(event, null);
	}

	public Guild addGuild(GuildJSON guild) {
		boolean exists = this.guilds.containsKey(guild.id);

		Guild newGuild = new Guild(this, guild);
		this.guilds.put(newGuild.id, newGuild);
		if (!exists && this.discSocket.status == constants.Status.READY) {
			this.emit(constants.Events.GUILD_CREATE, newGuild);
		}
		return newGuild;
	}

	public Channel addChannel(ChannelJSON data) {
		return this.addChannel(data, null);
	}

	public Channel addChannel(ChannelJSON data, Guild guild) {
		boolean exists = this.channels.containsKey(data.id);
		Channel channel = null;
		if (data.type == constants.ChannelTypes.DM) {
			channel = new PrivateChannel(this, data);
		} else if (data.type == constants.ChannelTypes.groupDM) {
			channel = new Channel(this, data);
		} else {
			if (guild != null) {
				if (data.type == constants.ChannelTypes.text) {
					channel = new TextChannel(guild, data);
					guild.channels.put(channel.id, (GuildChannel) channel);					
				} else if (data.type == constants.ChannelTypes.voice) {
					channel = new VoiceChannel(guild, data);
					guild.channels.put(channel.id, (GuildChannel) channel);	
				}
			}
		}

		if (channel != null) {
			this.channels.put(channel.id, channel);
			if (!exists && this.ready) {
				this.emit(constants.Events.CHANNEL_CREATE, channel);
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

	public int resolvePermission(String permission) {
		int resolved = 0;
		try {
			@SuppressWarnings("rawtypes")
			Class permFlags = Class.forName("constants.PermissionFlags");
			Field[] flags = permFlags.getFields();
			for (Field f : flags) {
				if (permission == f.getName()) {
					try {
						resolved = f.getInt(f);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return resolved;
	}

	public void checkReady() {
		if (this.discSocket.status != constants.Status.READY && this.discSocket.status != constants.Status.NEARLY) {
			int unavailable = 0;
			Collection<Guild> guilds = this.guilds.values();
			for (Guild guild : guilds) {
				unavailable += guild.available ? 0 : 1;
			}
			if (unavailable == 0) {
				this.discSocket.status = constants.Status.NEARLY;
				this.emitReady();
			}
		}
	}

	public void emitReady() {
		this.discSocket.status = constants.Status.READY;
		this.ready = true;
		this.emit(constants.Events.READY, this);
	}

}
