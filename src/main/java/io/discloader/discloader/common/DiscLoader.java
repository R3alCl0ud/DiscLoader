package io.discloader.discloader.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;

import com.neovisionaries.ws.client.WebSocketException;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.client.registry.ClientRegistry;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.common.event.DisconnectEvent;
import io.discloader.discloader.common.event.EventManager;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.core.entity.invite.Invite;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GatewayJSON;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.rest.RESTManager;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.util.Methods;
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
 * 	// time to login
 * 	loader.login(TOKEN);
 *
 * }
 * </pre>
 * 
 * @author Perry Berman, Zach Waldron
 */
public class DiscLoader {

	public static final Logger LOG = new DLLogger("DiscLoader").getLogger();

	public static DiscLoader getDiscLoader() {
		return ModRegistry.loader;
	}

	private Shard shard = null;

	public final List<IEventListener> handlers;

	public final Gateway socket;

	public String token;

	public boolean ready;

	private EventManager eventManager;

	public final ClientRegistry clientRegistry;

	public RESTManager rest;

	public int shards, shardid;

	// public Map<Long, IUser> users;
	// public Map<Long, IChannel> channels;
	// public Map<Long, IGroupChannel> groupChannels;
	// public Map<Long, IGuildChannel> guildChannels;
	// public Map<Long, IPrivateChannel> privateChannels;
	// public Map<Long, ITextChannel> textChannels;
	// public Map<Long, IVoiceChannel> voiceChannels;
	// public Map<Long, VoiceConnection> voiceConnections;

	private CompletableFuture<DiscLoader> rf;
	private Map<Long, IGuild> syncingGuilds;


	/**
	 * The User we are currently logged in as.
	 * 
	 */
	public DLUser user;

	private DLOptions options;

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
	 * @param options
	 *            Options to be passed to the client
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
	 * @param shards
	 *            The total number of shards
	 * @param shard
	 *            The number id of this shard
	 * @since 0.0.3
	 */
	public DiscLoader(int shard, int shards) {
		this.shards = shards;
		this.shardid = shard;
		if (!(System.out instanceof DLPrintStream))
			System.setOut(new DLPrintStream(System.out, LOG));
		if (!(System.err instanceof DLErrorStream))
			System.setErr(new DLErrorStream(System.err, LOG));
		System.setProperty("http.agent", "DiscLoader");
		socket = new Gateway(this);
		rest = new RESTManager(this);
		clientRegistry = new ClientRegistry();
		syncingGuilds = new HashMap<>();
		ready = false;
		eventManager = new EventManager();
		handlers = eventManager.getHandlers();
		ModRegistry.loader = this;
		options = new DLOptions();
	}

	public DiscLoader(Shard shard) {
		this(shard.getShardID(), shard.getShardCount());
		this.shard = shard;
	}

	/**
	 * @deprecated Use {@link #addEventListener(IEventListener)} instead
	 */
	public DiscLoader addEventHandler(IEventListener e) {
		return addEventListener(e);
	}

	/**
	 * Adds an event listener to the client.
	 * 
	 * @param e The IEventListener to add
	 * @return {@code this}
	 */
	public DiscLoader addEventListener(IEventListener e) {
		eventManager.addEventHandler(e);
		return this;
	}

	public void checkReady() {
		try {
			if (socket.status != Status.READY && socket.status != Status.NEARLY) {
				int unavailable = 0;
				if (shard == null) {
					for (IGuild guild : EntityRegistry.getGuilds()) {
						unavailable += guild.isAvailable() ? 0 : 1;
					}
				} else {
					for (IGuild guild : EntityRegistry.getGuildsOnShard(shard)) {
						unavailable += guild.isAvailable() ? 0 : 1;
					}
				}
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

	public CompletableFuture<Void> disconnect() {
		return disconnect(1000, null);
	}

	public CompletableFuture<Void> disconnect(int code) {
		return disconnect(code, null);
	}

	public CompletableFuture<Void> disconnect(int code, String reason) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		onceEvent(DisconnectEvent.class, (e) -> {
			future.complete(null);
		});
		socket.ws.disconnect(code, reason);
		return future;
	}

	public void emit(DLEvent event) {
		eventManager.emit(event);
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
		rf.complete(this);
		CommandHandler.handleCommands = true;
		ReadyEvent event = new ReadyEvent(this);
		emit(DLUtil.Events.READY, event);
		emit(event);
	}

	/**
	 * @param code
	 * @return A CompletableFuture that completes with an IInvite object if successful.
	 */
	public CompletableFuture<IInvite> getInvite(String code) {
		CompletableFuture<IInvite> future = new CompletableFuture<>();
		CompletableFuture<InviteJSON> cf = rest.request(Methods.GET, Endpoints.invite(code), new RESTOptions(), InviteJSON.class);
		cf.thenAcceptAsync(inviteJSON -> {
			future.complete(new Invite(inviteJSON, this));
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Returns the client options.
	 * 
	 * @return the options
	 */
	public DLOptions getOptions() {
		return options;
	}

	/**
	 * Returns a shard object if the connection is sharded.
	 * 
	 * @return the shard
	 */
	public Shard getShard() {
		return this.shard;
	}
	
	/**
	 * Returns a DLUser object representing the user you are logged in as.
	 * 
	 * @return a DLUser object representing the user you are logged in as.
	 */
	public DLUser getSelfUser() {
		return user;
	}

	public boolean isGuildSyncing(IGuild guild) {
		return syncingGuilds.containsKey(guild.getID());
	}

	public boolean isGuildSyncing(String guildID) {
		return syncingGuilds.containsKey(SnowflakeUtil.parse(guildID));
	}

	/**
	 * Makes the client log into the gateway. using a predefined token.<br>
	 * You can use {@link DLOptions} to set the token when you create a new
	 * DiscLoader object.
	 * 
	 * @return A CompletableFuture that completes with {@code this} if successful.
	 */
	public CompletableFuture<DiscLoader> login() {
		return login(token);
	}

	/**
	 * Connects the current instance of the {@link DiscLoader loader} into Discord's
	 * gateway servers
	 * 
	 * @param token
	 *            your API token
	 * @return A CompletableFuture that completes with {@code this} if successful.
	 */
	public CompletableFuture<DiscLoader> login(String token) {
		LOG.info("Attempting to login");
		rf = new CompletableFuture<>();

		Command.registerCommands();
		this.token = token;
		CompletableFuture<GatewayJSON> cf = rest.request(Methods.GET, Endpoints.gateway, new RESTOptions(), GatewayJSON.class);
		cf.thenAcceptAsync(gateway -> {
			try {
				socket.connectSocket(gateway.url + DLUtil.GatewaySuffix);
			} catch (WebSocketException | IOException e1) {
				e1.printStackTrace();
				rf.completeExceptionally(e1);
			}
		});
		cf.exceptionally(e -> {
			rf.completeExceptionally(e);
			return null;
		});

		return rf;
	}

	public CompletableFuture<Void> logout() {
		return disconnect(1000, null);
	}

	public CompletableFuture<Void> logout(int code) {
		return disconnect(code, null);
	}

	public CompletableFuture<Void> logout(int code, String reason) {
		return disconnect(code, reason);
	}

	public CompletableFuture<Void> logout(String reason) {
		return disconnect(1000, reason);
	}

	public <T> DiscLoader onceEvent(Class<T> cls, Consumer<Object> consumer) {
		eventManager.onceEvent(cls, consumer);
		return this;
	}

	public DiscLoader onceEvent(Consumer<Object> consumer, Function<IGuild, Boolean> checker) {
		eventManager.onceEvent(consumer, checker);
		return this;
	}

	public <T> DiscLoader onEvent(Class<T> cls, Consumer<Object> consumer) {
		eventManager.onEvent(cls, consumer);
		return this;
	}

	/**
	 * @deprecated Use {@link #removeEventListener(IEventListener)} instead
	 */
	public DiscLoader removeEventHandler(IEventListener e) {
		return removeEventListener(e);
	}

	public DiscLoader removeEventListener(IEventListener eventListener) {
		eventManager.removeEventHandler(eventListener);
		return this;
	}

	/**
	 * This method gets called in {@link #login(String)} before attempting to login
	 * now.<br>
	 * <br>
	 * <strike>This method <u><b>must</b></u> be called to start DiscLoader. <br>
	 * As it begins the setup process for DiscLoader to be able to function
	 * correctly.<br>
	 * It <u><b>will</b></u> crash otherwise</strike>
	 * 
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	// private CompletableFuture<Void> startup() {
	// if (ModRegistry.loaded.isDone()) {
	// return CompletableFuture.completedFuture((Void) null);
	// }
	// if (!(System.out instanceof DLPrintStream)) System.setOut(new
	// DLPrintStream(System.out, LOG));
	// if (!(System.err instanceof DLErrorStream)) System.setErr(new
	// DLErrorStream(System.err, LOG));
	// System.setProperty("http.agent", "DiscLoader");
	// if (Main.usegui == true) {
	// Main.window = new WindowFrame(this);
	// ProgressLogger.stage(1, 3, "Mod Discovery");
	// ModDiscoverer.checkModDir();
	// ArrayList<ModCandidate> candidates = ModDiscoverer.discoverMods();
	// ProgressLogger.stage(2, 3, "Discovering Mod Containers");
	// return ModRegistry.checkCandidates(candidates);
	// } else {
	// ProgressLogger.stage(1, 3, "Mod Discovery");
	// LOG.info("Beginning Mod Discovery");
	// ModDiscoverer.checkModDir();
	// ArrayList<ModCandidate> candidates = ModDiscoverer.discoverMods();
	// ProgressLogger.stage(2, 3, "Discovering Mod Containers");
	// LOG.info("Discovering Mod Containers");
	// return ModRegistry.checkCandidates(candidates);
	// }
	// }

	/**
	 * Sets the clients options;
	 * 
	 * @param options
	 *            The new options to use
	 * @return this.
	 */
	public DiscLoader setOptions(DLOptions options) {
		shardid = options.shard;
		shards = options.shards;
		token = options.token;
		Command.defaultCommands = options.defaultCommands;
		CommandHandler.prefix = options.prefix;
		this.options = options;
		return this;
	}

	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guilds
	 *            the guilds to sync
	 * @throws GuildSyncException
	 * @throws AccountTypeException
	 */
	public void syncGuilds(IGuild... guilds) throws GuildSyncException, AccountTypeException {
		if (user.isBot())
			throw new AccountTypeException("Only user accounts are allowed to sync guilds");

		String[] ids = new String[guilds.length];
		for (int i = 0; i < guilds.length; i++) {
			if (isGuildSyncing(guilds[i]))
				throw new GuildSyncException("Cannot syncing a guild that is currently syncing");
			ids[i] = Long.toUnsignedString(guilds[i].getID());
		}

		Packet packet = new Packet(12, ids);
		socket.send(packet, true);
	}

	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guildIDs
	 *            the ids of the guilds to sync
	 * @throws AccountTypeException
	 * @throws GuildSyncException
	 */
	public void syncGuilds(long... guildIDs) throws AccountTypeException, GuildSyncException {
		if (user.isBot())
			throw new AccountTypeException("Only user accounts are allowed to sync guilds");

		String[] ids = new String[guildIDs.length];
		for (int i = 0; i < guildIDs.length; i++) {
			ids[i] = Long.toUnsignedString(guildIDs[i], 10);
		}

		Packet packet = new Packet(12, ids);
		socket.send(packet, true);
	}

	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guildIDs
	 *            the ids of the guilds to sync
	 * @throws AccountTypeException
	 * @throws GuildSyncException
	 */
	public void syncGuilds(String... guildIDs) throws AccountTypeException, GuildSyncException {
		if (user.isBot())
			throw new AccountTypeException("Only user accounts are allowed to sync guilds");

		for (String id : guildIDs) {
			if (isGuildSyncing(id))
				throw new GuildSyncException("Cannot syncing a guild that is currently syncing");
		}

		Packet packet = new Packet(12, guildIDs);
		socket.send(packet, true);
	}

}
