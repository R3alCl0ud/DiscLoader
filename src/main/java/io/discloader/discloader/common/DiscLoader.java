package io.discloader.discloader.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.registry.ClientRegistry;
import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.discovery.ModDiscoverer;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.event.UserUpdateEvent;
import io.discloader.discloader.common.event.channel.ChannelCreateEvent;
import io.discloader.discloader.common.event.channel.ChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.ChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelCreateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.GuildChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.TypingStartEvent;
import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.common.event.guild.GuildBanRemoveEvent;
import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.common.event.guild.GuildDeleteEvent;
import io.discloader.discloader.common.event.guild.GuildSyncEvent;
import io.discloader.discloader.common.event.guild.GuildUpdateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleDeleteEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GuildMessageUpdateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageDeleteEvent;
import io.discloader.discloader.common.event.message.PrivateMessageUpdateEvent;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.common.start.Main;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.rest.RESTManager;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * The DiscLoader client object <br>
 * <H1>How To Use</H1>
 * 
 * <pre>
 * 
 * public static void main(String... args) {
 * 	// create a new instance of DiscLoader
 * 	DiscLoader loader = new DiscLoader();
 * 
 * 	// make it do it's startup stuff
 * 	loader.startup();
 * 
 * 	// since it's probably done, time to login
 * 	loader.login(TOKEN);
 *
 * }
 * </pre>
 * 
 * @author Perry Berman, Zach Waldron
 */
public class DiscLoader {

	private class Gateway {

		public String url;
	}

	public static final Logger LOG = new DLLogger("DiscLoader").getLogger();

	public static DiscLoader getDiscLoader() {
		return ModRegistry.loader;
	}

	public final ArrayList<IEventListener> handlers = new ArrayList<>();

	public final DiscSocket socket;

	public String token;

	public boolean ready;

	public final ClientRegistry clientRegistry;

	public RESTManager rest;

	public int shards;

	public int shard;

	private CompletableFuture<String> future;

	/**
	 * A HashMap of the client's cached users. Indexed by {@link User#id}.
	 * 
	 * @author Perry Berman
	 * @see User
	 * @see HashMap
	 */
	public HashMap<Long, IUser> users;

	/**
	 * A HashMap of the client's cached channels. Indexed by {@link Channel#id}.
	 * 
	 * @author Perry Berman
	 * @see Channel
	 * @see HashMap
	 */
	public HashMap<Long, IChannel> channels;

	/**
	 * A HashMap of the client's cached groupDM channels. Indexed by
	 * {@link Channel#id}
	 * 
	 * @author Perry Berman
	 */
	public HashMap<Long, IGroupChannel> groupChannels;

	public HashMap<Long, IGuildChannel> guildChannels;

	/**
	 * A HashMap of the client's cached PrivateChannels. Indexed by
	 * {@link Channel#id}.
	 * 
	 * @see Channel
	 * @see PrivateChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<Long, IPrivateChannel> privateChannels;

	/**
	 * A HashMap of the client's cached TextChannels. Indexed by
	 * {@link Channel#id}.
	 * 
	 * @see Channel
	 * @see TextChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<Long, ITextChannel> textChannels;

	/**
	 * A HashMap of the client's cached VoiceChannels. Indexed by
	 * {@link Channel#id}.
	 * 
	 * @see Channel
	 * @see VoiceChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<Long, IVoiceChannel> voiceChannels;

	/**
	 * A HashMap of the client's voice connections. Indexed by {@link Guild#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	public HashMap<Long, VoiceConnection> voiceConnections;

	/**
	 * A HashMap of the client's cached Guilds. Indexed by {@link Guild#id}
	 * 
	 * @see Guild
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<Long, IGuild> guilds;

	private HashMap<Long, IGuild> syncingGuilds;

	/**
	 * The User we are currently logged in as.
	 * 
	 * @author Perry Berman
	 */
	public DLUser user;

	public Timer timer;

	private boolean started = false;

	/**
	 * The DiscLoader client object <br>
	 * <H1>How To Use</H1>
	 * 
	 * <pre>
	 * 
	 * public static void main(String... args) {
	 * 	// create a new instance of DiscLoader
	 * 	DiscLoader loader = new DiscLoader();
	 * 
	 * 	// time to login
	 * 	loader.login(TOKEN);
	 *
	 * }
	 * </pre>
	 */
	public DiscLoader() {
		this(1, 0);
	}

	/**
	 * <pre>
	 * 
	 * public static void main(String... args) {
	 * 	DLOptions options = new DLOptions("TOKEN", "PREFIX");
	 * 
	 * 	// create a new instance of DiscLoader
	 * 	DiscLoader loader = new DiscLoader(options);
	 * 
	 * 	// time to login
	 * 	loader.login();
	 *
	 * }
	 * </pre>
	 * 
	 * @param options Options to be passed to the client
	 */
	public DiscLoader(DLOptions options) {
		this(options.shard, options.shards);
		setOptions(options);
	}

	/**
	 * The DiscLoader client object <br>
	 * <H1>How To Use</H1>
	 * <H1>Shard Manager jar</H1>
	 * 
	 * <pre>
	 * 
	 * public static void main(String... args) {
	 * 	int shards = 10;
	 * 
	 * 	ShardManager manager = new ShardManager(shards);
	 * 	manager.start();
	 * 
	 * }
	 * 
	 * </pre>
	 * 
	 * <H1>Shard jar</H1>
	 * 
	 * <pre>
	 * 
	 * // main method in shard jar
	 * public static void main(String... args) {
	 * 	// create a new instance of DiscLoader shard
	 * 	DiscLoader loader = new DiscLoader(System.getenv("shards"), System.getenv("shard"));
	 * 
	 * 	// make it do it's startup stuff
	 * 	loader.startup();
	 * 
	 * 	// since it's probably done, time to login
	 * 	loader.login(TOKEN);
	 *
	 * }
	 * </pre>
	 * 
	 * @param shards The total number of shards
	 * @param shard The number id of this shard
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	public DiscLoader(int shards, int shard) {
		this.shards = shards;
		this.shard = shard;
		socket = new DiscSocket(this);
		rest = new RESTManager(this);
		clientRegistry = new ClientRegistry();
		users = new HashMap<>();
		channels = new HashMap<>();
		groupChannels = new HashMap<>();
		privateChannels = new HashMap<>();
		textChannels = new HashMap<>();
		voiceChannels = new HashMap<>();
		voiceConnections = new HashMap<>();
		guilds = new HashMap<>();
		syncingGuilds = new HashMap<>();
		timer = new Timer();
		ready = false;

		ModRegistry.loader = this;

	}

	public IChannel addChannel(ChannelJSON data) {
		return this.addChannel(data, null);
	}

	public IChannel addChannel(ChannelJSON data, IGuild guild) {
		boolean exists = this.channels.containsKey(data.id);
		IChannel channel = null;
		if (data.type == DLUtil.ChannelTypes.DM) {
			channel = new PrivateChannel(this, data);
		} else if (data.type == DLUtil.ChannelTypes.groupDM) {
			channel = new Channel(this, data);
		} else {
			if (guild != null) {
				if (data.type == DLUtil.ChannelTypes.text) {
					channel = new TextChannel(guild, data);
					guild.getTextChannels().put(channel.getID(), (TextChannel) channel);
				} else if (data.type == DLUtil.ChannelTypes.voice) {
					channel = new VoiceChannel(guild, data);
					guild.getVoiceChannels().put(channel.getID(), (VoiceChannel) channel);
				}
			}
		}

		if (channel != null) {
			switch (channel.getType()) {
			case TEXT:
				this.textChannels.put(channel.getID(), (TextChannel) channel);
				break;
			case DM:
				this.privateChannels.put(channel.getID(), (PrivateChannel) channel);
				break;
			case VOICE:
				this.voiceChannels.put(channel.getID(), (VoiceChannel) channel);
				break;
			default:
				this.channels.put(channel.getID(), channel);
			}
			this.channels.put(channel.getID(), channel);
			if (!exists && this.ready) {
				this.emit(DLUtil.Events.CHANNEL_CREATE, channel);
			}
			return channel;
		}

		return null;
	}

	public void addEventHandler(IEventListener e) {
		handlers.add(e);
	}

	public IGuild addGuild(GuildJSON guild) {
		IGuild newGuild = new Guild(this, guild);

		boolean exists = guilds.containsKey(newGuild.getID());
		try {
			guilds.put(newGuild.getID(), newGuild);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!exists && socket.status == DLUtil.Status.READY) {
			GuildCreateEvent event = new GuildCreateEvent(newGuild);
			emit(event);
			emit(DLUtil.Events.GUILD_CREATE, event);
		}
		return newGuild;
	}

	public IUser addUser(UserJSON data) {
		if (this.users.containsKey(data.id)) return this.users.get(data.id);
		User user = new User(this, data);
		this.users.put(user.getID(), user);
		return user;
	}

	public void checkReady() {
		try {
			if (socket.status != DLUtil.Status.READY && socket.status != DLUtil.Status.NEARLY) {
				int unavailable = 0;
				for (IGuild guild : guilds.values()) {
					unavailable += guild.isAvailable() ? 0 : 1;
				}
				ProgressLogger.progress(guilds.size() - unavailable, guilds.size(), "Guilds Cached");
				if (unavailable == 0) {

					socket.status = Status.NEARLY;
					try {
						emitReady();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doneLoading() {
		future.complete("ready");
	}

	public void disconnect() {
		socket.ws.disconnect(1000);
	}

	public void emit(DLEvent event) {
		for (IEventListener handler : handlers) {
			if (event instanceof DLPreInitEvent) {
				handler.PreInit((DLPreInitEvent) event);
			} else if (event instanceof RawEvent) {
				handler.RawPacket((RawEvent) event);
			} else if (event instanceof ReadyEvent) {
				handler.Ready((ReadyEvent) event);
			} else if (event instanceof GuildChannelCreateEvent) {
				handler.GuildChannelCreate((GuildChannelCreateEvent) event);
			} else if (event instanceof ChannelCreateEvent) {
				handler.ChannelCreate((ChannelCreateEvent) event);
			} else if (event instanceof GuildChannelDeleteEvent) {
				handler.GuildChannelDelete((GuildChannelDeleteEvent) event);
			} else if (event instanceof ChannelDeleteEvent) {
				handler.ChannelDelete((ChannelDeleteEvent) event);
			} else if (event instanceof GuildChannelUpdateEvent) {
				handler.GuildChannelUpdate((GuildChannelUpdateEvent) event);
			} else if (event instanceof ChannelUpdateEvent) {
				handler.ChannelUpdate((ChannelUpdateEvent) event);
			} else if (event instanceof GuildCreateEvent) {
				handler.GuildCreate((GuildCreateEvent) event);
			} else if (event instanceof GuildDeleteEvent) {
				handler.GuildDelete((GuildDeleteEvent) event);
			} else if (event instanceof GuildUpdateEvent) {
				handler.GuildUpdate((GuildUpdateEvent) event);
			} else if (event instanceof GuildBanAddEvent) {
				handler.GuildBanAdd((GuildBanAddEvent) event);
			} else if (event instanceof GuildBanRemoveEvent) {
				handler.GuildBanRemove((GuildBanRemoveEvent) event);
			} else if (event instanceof GuildMemberAddEvent) {
				handler.GuildMemberAdd((GuildMemberAddEvent) event);
			} else if (event instanceof GuildEmojiCreateEvent) {
				handler.GuildEmojiCreate((GuildEmojiCreateEvent) event);
			} else if (event instanceof GuildEmojiDeleteEvent) {
				handler.GuildEmojiDelete((GuildEmojiDeleteEvent) event);
			} else if (event instanceof GuildEmojiUpdateEvent) {
				handler.GuildEmojiUpdate((GuildEmojiUpdateEvent) event);
			} else if (event instanceof GuildMemberRemoveEvent) {
				handler.GuildMemberRemove((GuildMemberRemoveEvent) event);
			} else if (event instanceof GuildMemberUpdateEvent) {
				handler.GuildMemberUpdate((GuildMemberUpdateEvent) event);
			} else if (event instanceof GuildMembersChunkEvent) {
				handler.GuildMembersChunk((GuildMembersChunkEvent) event);
			} else if (event instanceof GuildRoleCreateEvent) {
				handler.GuildRoleCreate((GuildRoleCreateEvent) event);
			} else if (event instanceof GuildRoleDeleteEvent) {
				handler.GuildRoleDelete((GuildRoleDeleteEvent) event);
			} else if (event instanceof GuildRoleUpdateEvent) {
				handler.GuildRoleUpdate((GuildRoleUpdateEvent) event);
			} else if (event instanceof GuildSyncEvent) {
				syncingGuilds.remove(((GuildSyncEvent) event).getGuild());
				handler.GuildSync((GuildSyncEvent) event);
			} else if (event instanceof GuildMessageCreateEvent) {
				handler.GuildMessageCreate((GuildMessageCreateEvent) event);
			} else if (event instanceof GuildMessageDeleteEvent) {
				handler.GuildMessageDelete((GuildMessageDeleteEvent) event);
			} else if (event instanceof GuildMessageUpdateEvent) {
				handler.GuildMessageUpdate((GuildMessageUpdateEvent) event);
			} else if (event instanceof PrivateMessageCreateEvent) {
				handler.PrivateMessageCreate((PrivateMessageCreateEvent) event);
			} else if (event instanceof GuildMessageDeleteEvent) {
				handler.PrivateMessageDelete((PrivateMessageDeleteEvent) event);
			} else if (event instanceof PrivateMessageUpdateEvent) {
				handler.PrivateMessageUpdate((PrivateMessageUpdateEvent) event);
			} else if (event instanceof MessageCreateEvent) {
				handler.MessageCreate((MessageCreateEvent) event);
			} else if (event instanceof MessageDeleteEvent) {
				handler.MessageDelete((MessageDeleteEvent) event);
			} else if (event instanceof MessageUpdateEvent) {
				handler.MessageUpdate((MessageUpdateEvent) event);
			} else if (event instanceof TypingStartEvent) {
				handler.TypingStart((TypingStartEvent) event);
			} else if (event instanceof UserUpdateEvent) {
				handler.UserUpdate((UserUpdateEvent) event);
			} else if (event instanceof VoiceStateUpdateEvent) {
				handler.VoiceStateUpdate((VoiceStateUpdateEvent) event);
			}
		}
	}

	public void emit(String event) {
		this.emit(event, null);
	}

	public void emit(String event, Object data) {
		for (ModContainer mod : ModRegistry.mods.values()) {
			mod.emit(event, data);
		}
	}

	public void emitReady() {
		socket.setReady();
		ready = true;
		CommandHandler.handleCommands = true;
		ReadyEvent event = new ReadyEvent(this);
		emit(DLUtil.Events.READY, event);
		emit(event);
	}

	public IGuild getGuild(String guildID) {
		return getGuild(SnowflakeUtil.parse(guildID));
	}

	public IGuild getGuild(long guildID) {
		return guilds.get(guildID);
	}

	/**
	 * Makes the client log into the gateway. using a predefined token.<br>
	 * You can use {@link DLOptions} to set the token when you create a new
	 * DiscLoader object.
	 * 
	 * @return {@literal CompletableFuture<String>}
	 */
	public CompletableFuture<DiscLoader> login() {
		return login(token);
	}

	/**
	 * Connects the current instance of the {@link DiscLoader loader} into
	 * Discord's gateway servers
	 * 
	 * @param token your API token
	 * @return A Future that completes with {@code this} if successful.
	 */
	public CompletableFuture<DiscLoader> login(String token) {
		future = new CompletableFuture<>();
		startup();
		future.join();
		this.token = token;

		CompletableFuture<DiscLoader> future2 = new CompletableFuture<>();
		try {
			rest.makeRequest(Endpoints.gateway, DLUtil.Methods.GET, true).thenAcceptAsync(text -> {
				Gson gson = new Gson();
				Gateway gateway = gson.fromJson(text, Gateway.class);
				try {
					socket.connectSocket(gateway.url + "?v=6&encoding=json");
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			// if (future2.isDone()) {
			// String text = future2.get();
			// Gson gson = new Gson();
			// Gateway gateway = gson.fromJson(text, Gateway.class);
			// try {
			// this.socket.connectSocket(gateway.url + "?v=6&encoding=json");
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return future2;
	}

	public void removeEventHandler(IEventListener e) {
		handlers.remove(e);
	}

	/**
	 * Sets the clients options;
	 * 
	 * @param options The new options to use
	 * @return this.
	 */
	public DiscLoader setOptions(DLOptions options) {
		shard = options.shard;
		shards = options.shards;
		token = options.token;
		Main.usegui = options.useWindow;
		Command.defaultCommands = options.defaultCommands;
		CommandHandler.prefix = options.prefix;
		return this;
	}

	public boolean isGuildSyncing(Guild guild) {
		return syncingGuilds.containsKey(guild.getID());
	}

	public boolean isGuildSyncing(String guildID) {
		return syncingGuilds.containsKey(guildID);
	}

	/**
	 * This method gets called in {@link #login(String)} before attempting to
	 * login now.<br>
	 * <br>
	 * <strike>This method <u><b>must</b></u> be called to start DiscLoader.
	 * <br>
	 * As it begins the setup process for DiscLoader to be able to function
	 * correctly.<br>
	 * It <u><b>will</b></u> crash otherwise</strike>
	 * 
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	private String startup() {
		if (started) {
			doneLoading();
			return "ready";
		}
		System.setOut(new DLPrintStream(System.out, LOG));
		System.setErr(new DLErrorStream(System.err, LOG));
		System.setProperty("http.agent", "DiscLoader");
		if (Main.usegui == true) {
			Main.window = new WindowFrame(this);
		} else {
			ProgressLogger.stage(1, 3, "Mod Discovery");
			ModDiscoverer.checkModDir();
			ArrayList<ModCandidate> candidates = ModDiscoverer.discoverMods();
			ProgressLogger.stage(2, 3, "Discovering Mod Containers");
			ModRegistry.checkCandidates(candidates);
		}
		started = true;
		return "ready";
	}

	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guildIDs the ids of the guilds to sync
	 * @throws AccountTypeException
	 * @throws GuildSyncException
	 */
	public void syncGuilds(String... guildIDs) throws AccountTypeException, GuildSyncException {
		if (user.bot) throw new AccountTypeException("Only user accounts are allowed to sync guilds");

		for (String id : guildIDs) {
			if (isGuildSyncing(id)) throw new GuildSyncException("Cannot syncing a guild that is currently syncing");
		}

		Packet packet = new Packet(12, guildIDs);
		socket.send(packet, true);
	}

	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guilds the guilds to sync
	 * @throws GuildSyncException
	 * @throws AccountTypeException
	 */
	public void syncGuilds(Guild... guilds) throws GuildSyncException, AccountTypeException {
		if (user.bot) throw new AccountTypeException("Only user accounts are allowed to sync guilds");

		String[] ids = new String[guilds.length];
		for (int i = 0; i < guilds.length; i++) {
			if (isGuildSyncing(guilds[i])) throw new GuildSyncException("Cannot syncing a guild that is currently syncing");
			ids[i] = Long.toUnsignedString(guilds[i].getID());
		}

		Packet packet = new Packet(12, ids);
		socket.send(packet, true);
	}

}
