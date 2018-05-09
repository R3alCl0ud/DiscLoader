package io.discloader.discloader.network.gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.DisconnectEvent;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.event.ReconnectEvent;
import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.common.logger.ProgressLogger;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.network.gateway.packets.AbstractHandler;
import io.discloader.discloader.network.gateway.packets.ChannelCreate;
import io.discloader.discloader.network.gateway.packets.ChannelDelete;
import io.discloader.discloader.network.gateway.packets.ChannelUpdate;
import io.discloader.discloader.network.gateway.packets.EmojiUpdate;
import io.discloader.discloader.network.gateway.packets.GuildBanAdd;
import io.discloader.discloader.network.gateway.packets.GuildBanRemove;
import io.discloader.discloader.network.gateway.packets.GuildCreate;
import io.discloader.discloader.network.gateway.packets.GuildDelete;
import io.discloader.discloader.network.gateway.packets.GuildMemberAdd;
import io.discloader.discloader.network.gateway.packets.GuildMemberRemove;
import io.discloader.discloader.network.gateway.packets.GuildMemberUpdate;
import io.discloader.discloader.network.gateway.packets.GuildMembersChunk;
import io.discloader.discloader.network.gateway.packets.GuildUpdate;
import io.discloader.discloader.network.gateway.packets.Hello;
import io.discloader.discloader.network.gateway.packets.MessageCreate;
import io.discloader.discloader.network.gateway.packets.MessageDelete;
import io.discloader.discloader.network.gateway.packets.MessageUpdate;
import io.discloader.discloader.network.gateway.packets.PresenceUpdate;
import io.discloader.discloader.network.gateway.packets.ReactionAdd;
import io.discloader.discloader.network.gateway.packets.ReactionRemove;
import io.discloader.discloader.network.gateway.packets.Ready;
import io.discloader.discloader.network.gateway.packets.Resumed;
import io.discloader.discloader.network.gateway.packets.RoleCreate;
import io.discloader.discloader.network.gateway.packets.RoleDelete;
import io.discloader.discloader.network.gateway.packets.RoleUpdate;
import io.discloader.discloader.network.gateway.packets.SocketPacket;
import io.discloader.discloader.network.gateway.packets.TypingStart;
import io.discloader.discloader.network.gateway.packets.VoiceServerUpdate;
import io.discloader.discloader.network.gateway.packets.VoiceStateUpdate;
import io.discloader.discloader.network.gateway.packets.request.GatewayIdentify;
import io.discloader.discloader.network.gateway.packets.request.GatewayResume;
import io.discloader.discloader.network.gateway.packets.request.Properties;
import io.discloader.discloader.network.json.GatewayJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.OPCodes;
import io.discloader.discloader.util.DLUtil.Status;
import io.discloader.discloader.util.DLUtil.WSEvents;

public class GatewayListener extends WebSocketAdapter {

	public Gson gson = new Gson();

	public DiscLoader loader;

	public Gateway gateway;

	private int tries = 0;

	private long timeout = 5000;

	private final Logger logger;

	public Map<String, AbstractHandler> handlers;

	public List<SocketPacket> queue;

	private String token;

	private final String logName, threadName;

	private Thread reconnection = null;

	private AtomicBoolean connected = new AtomicBoolean();

	public GatewayListener(Gateway gateway) {
		this.gateway = gateway;
		this.loader = this.gateway.loader;
		this.handlers = new HashMap<String, AbstractHandler>();
		this.queue = new ArrayList<SocketPacket>();
		this.logName = loader.shards > 1 ? "Gateway Listener (Shard: #" + loader.shardid + ")" : "Gateway Listener";
		this.logger = DLLogger.getLogger(logName);
		this.threadName = loader.shards > 1 ? "Gateway (Shard: #" + loader.shardid + ") - Reader" : "Gateway Reader";
		this.register(WSEvents.HELLO, new Hello(this.gateway));
		this.register(WSEvents.READY, new Ready(this.gateway));
		this.register(WSEvents.RESUMED, new Resumed(this.gateway));
		this.register(WSEvents.GUILD_CREATE, new GuildCreate(this.gateway));
		this.register(WSEvents.GUILD_BAN_ADD, new GuildBanAdd(this.gateway));
		this.register(WSEvents.GUILD_BAN_REMOVE, new GuildBanRemove(this.gateway));
		this.register(WSEvents.GUILD_DELETE, new GuildDelete(this.gateway));
		this.register(WSEvents.GUILD_UPDATE, new GuildUpdate(this.gateway));
		this.register(WSEvents.GUILD_ROLE_CREATE, new RoleCreate(this.gateway));
		this.register(WSEvents.GUILD_ROLE_DELETE, new RoleDelete(this.gateway));
		this.register(WSEvents.GUILD_ROLE_UPDATE, new RoleUpdate(this.gateway));
		this.register(WSEvents.GUILD_MEMBER_ADD, new GuildMemberAdd(this.gateway));
		this.register(WSEvents.GUILD_MEMBER_REMOVE, new GuildMemberRemove(this.gateway));
		this.register(WSEvents.GUILD_MEMBER_UPDATE, new GuildMemberUpdate(this.gateway));
		this.register(WSEvents.GUILD_MEMBERS_CHUNK, new GuildMembersChunk(this.gateway));
		this.register(WSEvents.GUILD_EMOJIS_UPDATE, new EmojiUpdate(this.gateway));
		this.register(WSEvents.CHANNEL_CREATE, new ChannelCreate(this.gateway));
		this.register(WSEvents.CHANNEL_DELETE, new ChannelDelete(this.gateway));
		this.register(WSEvents.CHANNEL_UPDATE, new ChannelUpdate(this.gateway));
		this.register(WSEvents.PRESENCE_UPDATE, new PresenceUpdate(this.gateway));
		this.register(WSEvents.MESSAGE_CREATE, new MessageCreate(this.gateway));
		this.register(WSEvents.MESSAGE_DELETE, new MessageDelete(this.gateway));
		this.register(WSEvents.MESSAGE_REACTION_ADD, new ReactionAdd(this.gateway));
		this.register(WSEvents.MESSAGE_REACTION_REMOVE, new ReactionRemove(this.gateway));
		this.register(WSEvents.MESSAGE_UPDATE, new MessageUpdate(this.gateway));
		this.register(WSEvents.TYPING_START, new TypingStart(this.gateway));
		this.register(WSEvents.VOICE_STATE_UPDATE, new VoiceStateUpdate(this.gateway));
		this.register(WSEvents.VOICE_SERVER_UPDATE, new VoiceServerUpdate(this.gateway));

	}

	public void handle(SocketPacket packet) {
		if (packet.op == OPCodes.RECONNECT) {
			if (loader.getOptions().isDebugging()) {
				logger.config("Received an instruction to reconnect to the gateway");
			}
			this.setSequence(packet.s);
			return;
		}

		if (packet.op == OPCodes.INVALID_SESSION) {
			if (loader.getOptions().isDebugging()) {
				logger.config("Session invalid. Sending an Identify.");
			}
			sendNewIdentify();
			return;
		}

		if (packet.op == OPCodes.HELLO) {
			this.handlers.get(WSEvents.HELLO).handle(packet);
			if (gateway.status.get() != Status.RECONNECTING) {
				sendNewIdentify();
			} else {
				sendResume();
			}
		}

		if (packet.op == DLUtil.OPCodes.HEARTBEAT_ACK) {
			gateway.lastHeartbeatAck.set(true);
			if (loader.getOptions().isDebugging()) {
				logger.config("Heartbeat Acknowledged");
			}
		} else if (packet.op == OPCodes.HEARTBEAT) {
			if (loader.getOptions().isDebugging()) {
				logger.config("Recieved Heartbeat request from Gateway.");
			}
			JSONObject payload = new JSONObject().put("op", OPCodes.HEARTBEAT).put("d", gateway.sequence);
			gateway.send(payload, true);
		}

		setSequence(packet.s);

		if (gateway.status.get() != Status.READY && gateway.status.get() != Status.RECONNECTING) {
			if (DLUtil.EventWhitelist.indexOf(packet.t) == -1) {
				queue.add(packet);
				return;
			}
		}

		if (packet.op == OPCodes.DISPATCH) {
			if (!handlers.containsKey(packet.t))
				return;
			try {
				handlers.get(packet.t).handle(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void handleQueue() {
		this.queue.forEach(packet -> {
			this.handle(packet);
			this.queue.remove(packet);
		});
	}

	public void onConnected(WebSocket ws, Map<String, List<String>> arg1) throws Exception {
		connected.set(true);
		/*
		 * Hoping this will fix that goddamn heartbeat not acknowledged when a heartbeat
		 * had never been sent in the first place bug.
		 */
		gateway.lastHeartbeatAck.set(true);
		if (loader.getOptions().isDebugging()) {
			logger.info("Connected to the gateway");
		}
		ProgressLogger.stage(2, 3, "Caching API Objects");
		if (Thread.currentThread().getName().equals("ReadingThread")) {
			Thread.currentThread().setName(threadName);
		}
		if (reconnection != null && !reconnection.isInterrupted()) {
			reconnection.interrupt();
			reconnection = null;
		}
	}

	public void onDisconnected(WebSocket ws, WebSocketFrame serverFrame, WebSocketFrame clientFrame, boolean isServer) throws Exception {
		gateway.killHeartbeat();
		loader.emit(new DisconnectEvent(loader, serverFrame, clientFrame, isServer));
		connected.set(false);
		if (isServer) {
			logger.severe(String.format("Gateway connection was closed by the server. Close Code: %d, Reason: %s", serverFrame != null ? serverFrame.getCloseCode() : 0, serverFrame != null ? serverFrame.getCloseReason() : null));
			if (serverFrame != null ? shouldResume(serverFrame) : true) {
				// if connection wasn't closed properly try to reconnect
				tryReconnecting();
			} else {
				connectToNewEndpoint();
			}
		} else {
			logger.severe(String.format("Client disconnected from the gateway, Close Code: %d, Reason: %s", clientFrame != null ? clientFrame.getCloseCode() : 0, clientFrame != null ? clientFrame.getCloseReason() : null));
			if (gateway.status.get() != Status.DISCONNECTING) {
				if (clientFrame != null ? shouldResume(clientFrame) : true) {
					// if connection wasn't closed properly try to reconnect
					tryReconnecting();
				} else {
					connectToNewEndpoint();
				}
			}
		}
		gateway.setStatus(Status.DISCONNECTED);
	}

	@Override
	public void onError(WebSocket websocket, WebSocketException ex) {
		if (loader.getOptions().isDebugging()) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onFrame(WebSocket ws, WebSocketFrame frame) {
		RawEvent event = new RawEvent(loader, frame);
		loader.emit(event);
	}

	@Override
	public void onTextMessage(WebSocket ws, String text) throws Exception {
		try {
			SocketPacket packet = gson.fromJson(text, SocketPacket.class);
			handle(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void register(String event, AbstractHandler handler) {
		this.handlers.put(event, handler);
	}

	public boolean shouldResume(WebSocketFrame socketFrame) {
		return tries < 3 && (socketFrame == null || (socketFrame.getCloseCode() != 4007 && socketFrame.getCloseCode() != 4004 && socketFrame.getCloseCode() != 4010 && socketFrame.getCloseCode() != 4011));
	}

	public void setRetries(int i) {
		tries = i;
	}

	public void sendNewIdentify() {
		if (!loader.token.startsWith("Bot ")) {
			token = loader.token;
		}
		if (loader.getOptions().isDebugging()) {
			logger.config(token);
		}
		GatewayIdentify payload = new GatewayIdentify(token, 250, new Properties("DiscLoader", "DiscLoader", "DiscLoader"));

		try {
			if (loader.shards > 1) {
				payload.setShard(loader.shardid, loader.shards);
			}
		} catch (NullPointerException e) {
			logger.throwing(e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName(), e);
		}
		Packet packet = new Packet(OPCodes.IDENTIFY, payload);
		gateway.send(packet, true);
		gateway.sequence.set(-1);
		tries = 0;
	}

	public void setSequence(int s) {
		if (gateway.sequence.get() < s) {
			gateway.sequence.set(s);
		}
	}

	public void sendResume() {
		if (tries >= 3) {
			sendNewIdentify();
			return;
		}
		Packet d = new Packet(OPCodes.RESUME, new GatewayResume(gateway.sessionID, token, gateway.sequence.get()));
		gateway.send(d, true);
	}

	public void tryReconnecting() {
		gateway.setStatus(Status.RECONNECTING);
		if (loader.getOptions().isDebugging()) {
			logger.config("Waiting to reconnect to the gateway");
		}
		reconnection = new Thread(logName + " Reconnector") {
			public void run() {
				if (gateway.status.get() == Status.RECONNECTING && !this.isInterrupted()) {
					try {
						while (gateway.status.get() == Status.RECONNECTING && !gateway.websocket.isOpen() && !this.isInterrupted() && tries < 3) {
							tries++;
							System.out.println(timeout * (tries));
							Thread.sleep(timeout * (tries));
							if (loader.getOptions().isDebugging()) {
								logger.config("Attempting to reconnect to the gateway");
							}
							loader.emit(new ReconnectEvent(loader, tries));
							gateway.websocket = gateway.websocket.recreate().setMissingCloseFrameAllowed(false).connect();
							Thread.sleep(41250);
							if (gateway.status.get() == Status.RECONNECTING && !connected.get()) {
								logger.severe("Failed to connect to the Gateway");
							}
						}
					} catch (InterruptedException | WebSocketException | IOException e) {
						if (gateway.status.get() == Status.RECONNECTING) {
							if (tries < 3) {
								tryReconnecting();
								return;
							}
						}
						if (tries >= 3) {
							connectToNewEndpoint();
						}
						this.interrupt();
						return;
					}
				}
			}
		};
		reconnection.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		reconnection.setDaemon(true);
		reconnection.start();
	}

	public void connectToNewEndpoint() {
		CompletableFuture<GatewayJSON> future = loader.fetchGateway();
		future.thenAcceptAsync(data -> {
			try {
				gateway.connectSocket(data.url);
			} catch (WebSocketException | IOException e) {
				connectToNewEndpoint();
			}
		});
		future.exceptionally(ex -> {
			connectToNewEndpoint();
			return null;
		});
	}
}
