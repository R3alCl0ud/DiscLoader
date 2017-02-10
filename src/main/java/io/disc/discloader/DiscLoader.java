package io.disc.discloader;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;

import io.disc.discloader.objects.annotations.Mod;
import io.disc.discloader.objects.gateway.ChannelJSON;
import io.disc.discloader.objects.gateway.GuildJSON;
import io.disc.discloader.objects.gateway.UserJSON;
import io.disc.discloader.objects.loader.ModHandler;
import io.disc.discloader.objects.structures.Channel;
import io.disc.discloader.objects.structures.Guild;
import io.disc.discloader.objects.structures.PrivateChannel;
import io.disc.discloader.objects.structures.TextChannel;
import io.disc.discloader.objects.structures.User;
import io.disc.discloader.objects.structures.VoiceChannel;
import io.disc.discloader.rest.DiscREST;
import io.disc.discloader.socket.DiscSocket;
import io.disc.discloader.util.constants;

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
	public HashMap<String, PrivateChannel> privateChannels;
	public HashMap<String, TextChannel> textChannels;
	public HashMap<String, VoiceChannel> voiceChannels;
	public HashMap<String, Guild> guilds;
	public HashMap<String, Mod> mods;

	public User user;

	public Timer timer;

	public DiscLoader() {
		this.discSocket = new DiscSocket(this);
		this.handler = new DiscHandler(this);
		this.rest = new DiscREST(this);
		this.handler.loadEvents();

		this.users = new HashMap<String, User>();

		this.channels = new HashMap<String, Channel>();
		this.privateChannels = new HashMap<String, PrivateChannel>();
		this.textChannels = new HashMap<String, TextChannel>();
		this.voiceChannels = new HashMap<String, VoiceChannel>();
		this.guilds = new HashMap<String, Guild>();
		this.mods = new HashMap<String, Mod>();

		this.modh = new ModHandler();

//		this.modh.beginLoader();

		this.timer = new Timer();

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
					guild.textChannels.put(channel.id, (TextChannel) channel);
				} else if (data.type == constants.ChannelTypes.voice) {
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
//		System.out.println(this.discSocket.status);
		this.ready = true;
		this.emit(constants.Events.READY, this);
	}

}
