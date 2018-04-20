package io.discloader.discloader.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.common.event.DisconnectEvent;
import io.discloader.discloader.common.event.EventManager;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.core.entity.invite.Invite;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.entity.guild.DefaultNotifications;
import io.discloader.discloader.entity.guild.ExplicitContentFilter;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.VerificationLevel;
import io.discloader.discloader.entity.guild.VoiceRegion;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GatewayJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.json.VoiceRegionJSON;
import io.discloader.discloader.network.rest.RESTManager;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;
import io.discloader.discloader.util.DLUtil;
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

	public static final Logger LOG = DLLogger.getLogger(DiscLoader.class);

	public static DiscLoader getDiscLoader() {
		return ModRegistry.loader;
	}

	private Shard shard = null;
	private final EventManager eventManager;
	private CompletableFuture<DiscLoader> rf;
	private Map<Long, IGuild> syncingGuilds;
	private long readyAt = 0l;

	public final List<IEventListener> handlers;
	public final Gateway socket;
	public final RESTManager rest;
	public String token = null;
	public boolean ready;
	public int shards, shardid;

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
	 * Adds an event listener to the client.
	 * 
	 * @param e
	 *            The IEventListener to add
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
					for (IGuild guild : EntityRegistry.getGuildsCollection()) {
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

	/**
	 * Creates a new {@link IGuild} with the specified name.
	 * <h2>For Bot Accounts</h2><br>
	 * This endpoint can be used only by bots in less than 10 guilds.
	 * 
	 * @param name
	 *            The new {@link IGuild}'s name. Must be {@code 2-100} characters.
	 * @return A {@link CompletableFuture} that completes with an {@link IGuild}
	 *         object of the newly created guild if successful.
	 * @throws UnauthorizedException
	 *             Thrown if the {@link #getSelfUser() user} you are logged in as is
	 *             a bot that is in more than 10 guilds.
	 */
	public CompletableFuture<IGuild> createGuild(String name) throws UnauthorizedException {
		return createGuild(name, null, null, VerificationLevel.NONE, DefaultNotifications.ALL_MESSAGES, ExplicitContentFilter.DISABLED);
	}

	/**
	 * Creates a new {@link IGuild} with the specified name.
	 * <h2>For Bot Accounts</h2><br>
	 * This endpoint can be used only by bots in less than 10 guilds.
	 * 
	 * @param name
	 *            The new {@link IGuild}'s name. Must be {@code 2-100} characters.
	 * @param region
	 *            The new {@link IGuild}'s voice server region.
	 * @return A {@link CompletableFuture} that completes with an {@link IGuild}
	 *         object of the newly created guild if successful.
	 * @throws UnauthorizedException
	 *             Thrown if the {@link #getSelfUser() user} you are logged in as is
	 *             a bot that is in more than 10 guilds.
	 */
	public CompletableFuture<IGuild> createGuild(String name, VoiceRegion region) throws UnauthorizedException {
		return createGuild(name, region, null, VerificationLevel.NONE, DefaultNotifications.ALL_MESSAGES, ExplicitContentFilter.DISABLED);
	}

	/**
	 * Creates a new {@link IGuild} with the specified name.
	 * <h2>For Bot Accounts</h2><br>
	 * This endpoint can be used only by bots in less than 10 guilds.
	 * 
	 * @param name
	 *            The new {@link IGuild}'s name. Must be {@code 2-100} characters in
	 *            length.
	 * @param region
	 *            The new {@link IGuild}'s voice server region.
	 * @param icon
	 *            A {@link File} pointing to the new {@link IGuild}'s icon.
	 * @param verificationLevel
	 *            Members of the server must meet the following criteria before they
	 *            can send messages in text channels or initiate a direct message
	 *            conversation. If a member has an assigned role this does not
	 *            apply.
	 * @param notificationLevel
	 *            This will determine whether members who have not explicitly set
	 *            their notification settings receive a notification for every
	 *            message sent in this server or not.
	 * @param filterLevel
	 *            Automatically scan and delete messages sent in this server that
	 *            contain explicit content. Please choose how broadly the filter
	 *            will apply to members in your server.
	 * @return A {@link CompletableFuture} that completes with an {@link IGuild}
	 *         object of the newly created guild if successful.
	 * @throws UnauthorizedException
	 *             Thrown if the {@link #getSelfUser() user} you are logged in as is
	 *             a bot that is in more than 10 guilds.
	 */
	public CompletableFuture<IGuild> createGuild(String name, VoiceRegion region, File icon, VerificationLevel verificationLevel, DefaultNotifications notificationLevel, ExplicitContentFilter filterLevel) throws UnauthorizedException {
		CompletableFuture<IGuild> future = new CompletableFuture<>();
		if (getSelfUser().isBot() && EntityRegistry.getGuilds().size() >= 10) {
			UnauthorizedException aex = new UnauthorizedException("This endpoint can be used only by bots in less than 10 guilds.");
			future.completeExceptionally(aex);
			throw aex;
		}
		JSONObject payload = new JSONObject().put("name", name);
		if (region != null) {
			payload.put("region", region.getID());
		}
		if (icon != null && icon.exists() && icon.isFile() && icon.canRead()) {
			String base64;
			try {
				base64 = "data:image/png;base64," + Base64.encodeBase64String(Files.readAllBytes(icon.toPath()));
				payload.put("icon", base64);
			} catch (IOException e) {
				future.completeExceptionally(e);
				return future;
			}
		}
		payload.put("verification_level", verificationLevel.ordinal());
		payload.put("default_message_notifications", notificationLevel.ordinal());
		payload.put("explicit_content_filter", filterLevel.ordinal());
		CompletableFuture<GuildJSON> cf = rest.request(Methods.POST, Endpoints.guilds, new RESTOptions(payload), GuildJSON.class);
		cf.thenAcceptAsync(data -> {
			future.complete(EntityBuilder.getGuildFactory().buildGuild(data));
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
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
		ModRegistry.emit(event);
		if (event instanceof DisconnectEvent) {
			DisconnectEvent devent = (DisconnectEvent) event;
			if ((devent.getClientFrame().getCloseCode() == 4004 || devent.getServerFrame().getCloseCode() == 4004) && !rf.isDone()) {
				rf.completeExceptionally(new UnauthorizedException("Authentication failed"));
			}
		}
	}

	public void emit(String event, Object data) {
		return;
	}

	public void emitReady() {
		socket.setReady();
		ready = true;
		readyAt = System.currentTimeMillis();
		rf.complete(this);
		CommandHandler.handleCommands = true;
		ReadyEvent event = new ReadyEvent(this);
		emit(event);
	}

	public CompletableFuture<IUser> fetchUser(long id) {
		CompletableFuture<IUser> future = new CompletableFuture<>();
		CompletableFuture<UserJSON> cf = rest.request(Methods.GET, Endpoints.user(id), new RESTOptions(), UserJSON.class);
		cf.thenAcceptAsync(data -> {
			future.complete(EntityRegistry.addUser(data));
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	public CompletableFuture<IUser> fetchUser(String id) {
		return fetchUser(SnowflakeUtil.parse(id));
	}

	public CompletableFuture<GatewayJSON> fetchGateway() {
		return rest.request(Methods.GET, (token == null || !token.startsWith("Bot ")) ? Endpoints.gateway : Endpoints.botGateway, new RESTOptions(), GatewayJSON.class);
	}

	public EventManager getEventManager() {
		return eventManager;
	}

	public Map<Long, IGuild> getGuilds() {
		if (shards != 1) {
			Map<Long, IGuild> guilds = new HashMap<>();
			for (IGuild guild : EntityRegistry.getGuildsOnShard(shardid, shards)) {
				guilds.put(guild.getID(), guild);
			}
			return guilds;
		}
		return EntityRegistry.getGuilds();
	}

	/**
	 * @param code
	 * @return A CompletableFuture that completes with an IInvite object if
	 *         successful.
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
	 * @return the readyAt
	 */
	public long getReadyAt() {
		return readyAt;
	}

	/**
	 * Returns a DLUser object representing the user you are logged in as.
	 * 
	 * @return a DLUser object representing the user you are logged in as.
	 */
	public DLUser getSelfUser() {
		return user;
	}

	/**
	 * Returns a shard object if the connection is sharded.
	 * 
	 * @return the shard
	 */
	public Shard getShard() {
		return shard;
	}

	public int getShardCount() {
		return shards;
	}

	public int getShardID() {
		return shardid;
	}

	public long getUptime() {
		return System.currentTimeMillis() - readyAt;
	}

	public CompletableFuture<List<VoiceRegion>> getVoiceRegions() {
		CompletableFuture<List<VoiceRegion>> future = new CompletableFuture<>();
		CompletableFuture<VoiceRegionJSON[]> cf = rest.request(Methods.GET, Endpoints.voiceRegions, new RESTOptions(), VoiceRegionJSON[].class);
		cf.thenAcceptAsync(regionJSONs -> {
			List<VoiceRegion> regions = new ArrayList<>();
			for (VoiceRegionJSON region : regionJSONs) {
				regions.add(new VoiceRegion(region));
			}
			future.complete(regions);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
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
		if (rf != null && !rf.isDone())
			return rf;
		rf = new CompletableFuture<>();
		if (options.shouldLoadMods()) {
			LOG.info("Attempting to load mods");
			try {
				ModRegistry.startMods().get();
			} catch (Exception ex) {
				LOG.severe("Error ocurred while attempting to load mods");
				LOG.throwing(ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName(), ex);
			}
		}

		LOG.info("Attempting to login");
		Command.registerCommands();
		this.token = token;
		// Endpoints.botGateway;
		CompletableFuture<GatewayJSON> cf = fetchGateway();
		cf.thenAcceptAsync(gateway -> {
			try {
				socket.connectSocket(gateway.url + DLUtil.GatewaySuffix);
			} catch (Exception ex) {
				ex.printStackTrace();
				rf.completeExceptionally(ex);
			}
		});
		cf.exceptionally(ex -> {
			ex.printStackTrace();
			rf.completeExceptionally(ex);
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

	public <T extends DLEvent> DiscLoader onceEvent(Class<T> cls, Consumer<T> consumer) {
		eventManager.onceEvent(cls, consumer);
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

	public <T extends DLEvent> DiscLoader onEvent(Class<T> cls, Consumer<T> consumer) {
		eventManager.onEvent(cls, consumer);
		return this;
	}

	public DiscLoader removeEventListener(IEventListener eventListener) {
		eventManager.removeEventHandler(eventListener);
		return this;
	}

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
