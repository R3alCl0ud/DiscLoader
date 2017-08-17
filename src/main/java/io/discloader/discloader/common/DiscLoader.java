package io.discloader.discloader.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import io.discloader.discloader.common.event.EventManager;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.common.start.Main;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GatewayJSON;
import io.discloader.discloader.network.rest.RESTManager;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.InviteAction;
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
	
	private Shard shard = null;
	
	public static DiscLoader getDiscLoader() {
		return ModRegistry.loader;
	}
	
	public final List<IEventListener> handlers;
	
	public final Gateway socket;
	
	public String token;
	
	public boolean ready;
	
	private EventManager eventManager;
	
	public final ClientRegistry clientRegistry;
	
	public RESTManager rest;
	
	public int shards;
	
	public int shardid;
	
	/**
	 * A HashMap of the client's cached users. Indexed by {@link User#id}.
	 * 
	 * @author Perry Berman
	 * @see User
	 * @see HashMap
	 */
	// public HashMap<Long, IUser> users;
	
	/**
	 * A HashMap of the client's cached channels. Indexed by {@link Channel#id}.
	 * 
	 * @author Perry Berman
	 * @see Channel
	 * @see HashMap
	 */
	// public HashMap<Long, IChannel> channels;
	
	/**
	 * A HashMap of the client's cached groupDM channels. Indexed by {@link Channel#id}
	 * 
	 * @author Perry Berman
	 */
	// public HashMap<Long, IGroupChannel> groupChannels;
	//
	// public HashMap<Long, IGuildChannel> guildChannels;
	
	/**
	 * A HashMap of the client's cached PrivateChannels. Indexed by {@link Channel#id}.
	 * 
	 * @see Channel
	 * @see PrivateChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	// public HashMap<Long, IPrivateChannel> privateChannels;
	
	/**
	 * A HashMap of the client's cached TextChannels. Indexed by {@link Channel#id}.
	 * 
	 * @see Channel
	 * @see TextChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	// public HashMap<Long, ITextChannel> textChannels;
	
	/**
	 * A HashMap of the client's cached VoiceChannels. Indexed by {@link Channel#id}.
	 * 
	 * @see Channel
	 * @see VoiceChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	// public HashMap<Long, IVoiceChannel> voiceChannels;
	
	private CompletableFuture<DiscLoader> rf;
	
	/**
	 * A HashMap of the client's voice connections. Indexed by {@link Guild#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	// public HashMap<Long, VoiceConnection> voiceConnections;
	
	private HashMap<Long, IGuild> syncingGuilds;
	
	// public Timer timer;
	
	/**
	 * The User we are currently logged in as.
	 * 
	 * @author Perry Berman
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
	
	public DiscLoader(Shard shard) {
		this(shard.getShardID(), shard.getShardCount());
		this.shard = shard;
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
	public DiscLoader(int shard, int shards) {
		this.shards = shards;
		this.shardid = shard;
		if (!(System.out instanceof DLPrintStream)) System.setOut(new DLPrintStream(System.out, LOG));
		if (!(System.err instanceof DLErrorStream)) System.setErr(new DLErrorStream(System.err, LOG));
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
	
	public void addEventHandler(IEventListener e) {
		eventManager.addEventHandler(e);
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
					// LOG.info("Nearly ready");
					socket.status = Status.NEARLY;
					/*
					 * Uncomment when you are sure that this will work if (!user.isBot()) { //
					 * System.out.println("Just checking something"); // List<CompletableFuture<Map<Long, IGuildMember>>> futures = new
					 * ArrayList<>(); // for (IGuild guild : EntityRegistry.getGuilds()) { // if (guild.isLarge() &&
					 * guild.getMembers().size() < guild.getMemberCount()) futures.add(guild.fetchMembers()); // } //
					 * System.out.println(futures.size()); // if (futures.size() > 0) { // CompletableFuture.allOf(futures.toArray(new
					 * CompletableFuture[0])).thenAcceptAsync(a -> emitReady()); // } else { // emitReady(); // } } else {
					 */
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
	
	public void disconnect() {
		socket.ws.disconnect(1000);
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
	
	public CompletableFuture<IInvite> getInvite(String code) {
		return new InviteAction(code, DLUtil.Methods.GET).execute();
	}
	
	/**
	 * @return the options
	 */
	public DLOptions getOptions() {
		return options;
	}
	
	public boolean isGuildSyncing(IGuild guild) {
		return syncingGuilds.containsKey(guild.getID());
	}
	
	public boolean isGuildSyncing(String guildID) {
		return syncingGuilds.containsKey(guildID);
	}
	
	/**
	 * Makes the client log into the gateway. using a predefined token.<br>
	 * You can use {@link DLOptions} to set the token when you create a new DiscLoader object.
	 * 
	 * @return {@literal CompletableFuture<String>}
	 */
	public CompletableFuture<DiscLoader> login() {
		return login(token);
	}
	
	/**
	 * Connects the current instance of the {@link DiscLoader loader} into Discord's gateway servers
	 * 
	 * @param token your API token
	 * @return A Future that completes with {@code this} if successful.
	 */
	public CompletableFuture<DiscLoader> login(String token) {
		rf = new CompletableFuture<>();
		// startup().thenAcceptAsync(n -> {
		Command.registerCommands();
		this.token = token;
		rest.request(Methods.GET, Endpoints.gateway, new RESTOptions(), GatewayJSON.class).thenAcceptAsync(gateway -> {
			try {
				socket.connectSocket(gateway.url + DLUtil.GatewaySuffix);
			} catch (WebSocketException | IOException e1) {
				e1.printStackTrace();
				rf.completeExceptionally(e1);
			}
		}).exceptionally(e -> {
			rf.completeExceptionally(e);
			return null;
		});
		// });
		
		return rf;
	}
	
	public void onceEvent(Consumer<DLEvent> consumer) {
		eventManager.onceEvent(consumer);
	}
	
	public void onceEvent(Consumer<DLEvent> consumer, Function<IGuild, Boolean> checker) {
		eventManager.onceEvent(consumer, checker);
	}
	
	public void onEvent(Consumer<DLEvent> consumer) {
		eventManager.onEvent(consumer);
	}
	
	public void removeEventHandler(IEventListener e) {
		eventManager.removeEventHandler(e);
	}
	
	/**
	 * Sets the clients options;
	 * 
	 * @param options The new options to use
	 * @return this.
	 */
	public DiscLoader setOptions(DLOptions options) {
		shardid = options.shard;
		shards = options.shards;
		token = options.token;
		Main.usegui = options.useWindow;
		Command.defaultCommands = options.defaultCommands;
		CommandHandler.prefix = options.prefix;
		this.options = options;
		return this;
	}
	
	/**
	 * This method gets called in {@link #login(String)} before attempting to login now.<br>
	 * <br>
	 * <strike>This method <u><b>must</b></u> be called to start DiscLoader. <br>
	 * As it begins the setup process for DiscLoader to be able to function correctly.<br>
	 * It <u><b>will</b></u> crash otherwise</strike>
	 * 
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	// private CompletableFuture<Void> startup() {
	// if (ModRegistry.loaded.isDone()) {
	// return CompletableFuture.completedFuture((Void) null);
	// }
	// if (!(System.out instanceof DLPrintStream)) System.setOut(new DLPrintStream(System.out, LOG));
	// if (!(System.err instanceof DLErrorStream)) System.setErr(new DLErrorStream(System.err, LOG));
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
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guilds the guilds to sync
	 * @throws GuildSyncException
	 * @throws AccountTypeException
	 */
	public void syncGuilds(IGuild... guilds) throws GuildSyncException, AccountTypeException {
		if (user.bot) throw new AccountTypeException("Only user accounts are allowed to sync guilds");
		
		String[] ids = new String[guilds.length];
		for (int i = 0; i < guilds.length; i++) {
			if (isGuildSyncing(guilds[i])) throw new GuildSyncException("Cannot syncing a guild that is currently syncing");
			ids[i] = Long.toUnsignedString(guilds[i].getID());
		}
		
		Packet packet = new Packet(12, ids);
		socket.send(packet, true);
	}
	
	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guildIDs the ids of the guilds to sync
	 * @throws AccountTypeException
	 * @throws GuildSyncException
	 */
	public void syncGuilds(String... guildIDs) throws AccountTypeException, GuildSyncException {
		if (user.isBot()) throw new AccountTypeException("Only user accounts are allowed to sync guilds");
		
		for (String id : guildIDs) {
			if (isGuildSyncing(id)) throw new GuildSyncException("Cannot syncing a guild that is currently syncing");
		}
		
		Packet packet = new Packet(12, guildIDs);
		socket.send(packet, true);
	}
	
	/**
	 * Syncs guilds to client if the logged in user is not a bot
	 * 
	 * @param guildIDs the ids of the guilds to sync
	 * @throws AccountTypeException
	 * @throws GuildSyncException
	 */
	public void syncGuilds(long... guildIDs) throws AccountTypeException, GuildSyncException {
		if (user.isBot()) throw new AccountTypeException("Only user accounts are allowed to sync guilds");
		
		String[] ids = new String[guildIDs.length];
		for (int i = 0; i < guildIDs.length; i++) {
			ids[i] = Long.toUnsignedString(guildIDs[i], 10);
		}
		
		Packet packet = new Packet(12, ids);
		socket.send(packet, true);
	}
	
	/**
	 * @return the shard
	 */
	public Shard getShard() {
		return this.shard;
	}
	
}
