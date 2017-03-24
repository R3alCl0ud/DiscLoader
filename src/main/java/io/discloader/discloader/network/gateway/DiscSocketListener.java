package io.discloader.discloader.network.gateway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.network.gateway.packets.ChannelCreate;
import io.discloader.discloader.network.gateway.packets.ChannelDelete;
import io.discloader.discloader.network.gateway.packets.ChannelUpdate;
import io.discloader.discloader.network.gateway.packets.AbstractHandler;
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
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.OPCodes;
import io.discloader.discloader.util.DLUtil.Status;
import io.discloader.discloader.util.DLUtil.WSEvents;

public class DiscSocketListener extends WebSocketAdapter {
	public Gson gson = new Gson();

	public DiscLoader loader;

	public DiscSocket socket;

	private int retries = 0;

	private long timeout = 5000;

	private final Logger logger = new DLLogger("Gateway Listener").getLogger();

	public HashMap<String, AbstractHandler> handlers;

	public List<SocketPacket> queue;

	private String token;

	private Thread reconnection = null;

	public DiscSocketListener(DiscSocket socket) {
		this.socket = socket;
		this.loader = this.socket.loader;
		this.handlers = new HashMap<String, AbstractHandler>();
		this.queue = new ArrayList<SocketPacket>();

		this.register(WSEvents.HELLO, new Hello(this.socket));
		this.register(WSEvents.READY, new Ready(this.socket));
		this.register(WSEvents.RESUMED, new Resumed(this.socket));
		this.register(WSEvents.GUILD_CREATE, new GuildCreate(this.socket));
		this.register(WSEvents.GUILD_BAN_ADD, new GuildBanAdd(this.socket));
		this.register(WSEvents.GUILD_BAN_REMOVE, new GuildBanRemove(this.socket));
		this.register(WSEvents.GUILD_DELETE, new GuildDelete(this.socket));
		this.register(WSEvents.GUILD_UPDATE, new GuildUpdate(this.socket));
		this.register(WSEvents.GUILD_ROLE_CREATE, new RoleCreate(this.socket));
		this.register(WSEvents.GUILD_ROLE_DELETE, new RoleDelete(this.socket));
		this.register(WSEvents.GUILD_ROLE_UPDATE, new RoleUpdate(this.socket));
		this.register(WSEvents.GUILD_MEMBER_ADD, new GuildMemberAdd(this.socket));
		this.register(WSEvents.GUILD_MEMBER_REMOVE, new GuildMemberRemove(this.socket));
		this.register(WSEvents.GUILD_MEMBER_UPDATE, new GuildMemberUpdate(this.socket));
		this.register(WSEvents.GUILD_MEMBERS_CHUNK, new GuildMembersChunk(this.socket));
		this.register(WSEvents.GUILD_EMOJIS_UPDATE, new EmojiUpdate(this.socket));
		this.register(WSEvents.CHANNEL_CREATE, new ChannelCreate(this.socket));
		this.register(WSEvents.CHANNEL_DELETE, new ChannelDelete(this.socket));
		this.register(WSEvents.CHANNEL_UPDATE, new ChannelUpdate(this.socket));
		this.register(WSEvents.PRESENCE_UPDATE, new PresenceUpdate(this.socket));
		this.register(WSEvents.MESSAGE_CREATE, new MessageCreate(this.socket));
		this.register(WSEvents.MESSAGE_UPDATE, new MessageUpdate(this.socket));
		this.register(WSEvents.MESSAGE_DELETE, new MessageDelete(this.socket));
		this.register(WSEvents.TYPING_START, new TypingStart(this.socket));
		this.register(WSEvents.VOICE_STATE_UPDATE, new VoiceStateUpdate(this.socket));
		this.register(WSEvents.VOICE_SERVER_UPDATE, new VoiceServerUpdate(this.socket));
	}

	public void handle(SocketPacket packet) {
		if (packet.op == OPCodes.RECONNECT) {
			this.setSequence(packet.s);
			return;
		}

		if (packet.op == OPCodes.INVALID_SESSION) {
			sendNewIdentify();
			return;
		}

		if (packet.op == OPCodes.HELLO) {
			this.handlers.get(WSEvents.HELLO).handle(packet);
		}

		if (packet.op == DLUtil.OPCodes.HEARTBEAT_ACK) {
			this.socket.lastHeartbeatAck = true;
			this.loader.emit("debug", "Heartbeat Acknowledged");
		} else if (packet.op == OPCodes.HEARTBEAT) {
			JSONObject payload = new JSONObject().put("op", OPCodes.HEARTBEAT).put("d", this.socket.s);
			this.socket.send(payload, true);
			this.loader.emit("debug", "Recieved gateway heartbeat");
		}

		this.setSequence(packet.s);

		if (this.socket.status != Status.READY && socket.status != Status.RECONNECTING) {
			if (DLUtil.EventWhitelist.indexOf(packet.t) == -1) {
				this.queue.add(packet);
				return;
			}
		}

		if (packet.op == OPCodes.DISPATCH) {
			if (!this.handlers.containsKey(packet.t)) return;
			this.handlers.get(packet.t).handle(packet);
		}
	}

	public void handleQueue() {
		this.queue.forEach(packet -> {
			this.handle(packet);
			this.queue.remove(packet);
		});
	}

	public void onConnected(WebSocket ws, Map<String, List<String>> arg1) throws Exception {
		logger.info("Connected to the gateway");
		ProgressLogger.stage(2, 3, "Caching API Objects");
		if (socket.status != Status.RECONNECTING) {
			this.socket.lastHeartbeatAck = true;
			this.sendNewIdentify();
		} else {
			sendResume();
		}
	}

	public void onDisconnected(WebSocket ws, WebSocketFrame frame_1, WebSocketFrame frame_2, boolean isServer) throws Exception {
		this.socket.killHeartbeat();
		if (isServer) {
			logger.severe(String.format("Gateway connection was closed by the server. Close Code: %d, Reason: %s", frame_1.getCloseCode(), frame_1.getCloseReason()));
			if (frame_1.getCloseCode() != 1000) {
				// if connection wasn't closed properly try to reconnect
				tryReconnecting();
			}
		} else {
			logger.severe(String.format("Client was disconnected from the gateway, Code: %d", frame_2.getCloseCode()));
			if (frame_2.getCloseCode() != 1000) {
				// if connection wasn't closed properly try to reconnect
				tryReconnecting();
			}
		}

	}

	@Override
	public void onFrame(WebSocket ws, WebSocketFrame frame) {
		RawEvent event = new RawEvent(loader, frame);
		loader.emit(event);
		loader.emit("RawPacket", event);
	}

	@Override
	public void onTextMessage(WebSocket ws, String text) throws Exception {
		SocketPacket packet = gson.fromJson(text, SocketPacket.class);
		this.handle(packet);
	}

	public void register(String event, AbstractHandler handler) {
		this.handlers.put(event, handler);
	}

	public void sendNewIdentify() {
		if (!loader.token.startsWith("Bot")) {
			token = loader.token;
		}

		JSONObject properties = new JSONObject().put("$os", "DiscLoader").put("$browser", "DiscLoader").put("$device", "DiscLoader");
		GatewayIdentify payload = new GatewayIdentify(token, 250, properties);

		try {
			if (this.loader.shards > 1) {
				payload.setShard(loader.shard, loader.shards);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		Packet packet = new Packet(OPCodes.IDENTIFY, payload);
		socket.send(packet, true);
		socket.s = -1;
		retries = 0;
	}

	public void setSequence(int s) {
		if (s > this.socket.s) this.socket.s = s;
	}

	public void sendResume() {
		if (retries > 3) {
			sendNewIdentify();
			return;
		}
		System.out.println(socket.sessionID != null);
		Packet d = new Packet(OPCodes.RESUME, new GatewayResume(socket.sessionID, token, socket.s));
		System.out.println(gson.toJson(d));
		socket.send(d, true);
	}

	public void tryReconnecting() {
		this.socket.status = Status.RECONNECTING;
		logger.info("Attempting to reconnect to the gateway");
		if (reconnection == null) {
			reconnection = new Thread("GatewayReconnector") {
				public void run() {
					if (socket.status == Status.RECONNECTING && !interrupted()) {
						retries++;
						try {
							Thread.sleep(timeout * retries);
						} catch (InterruptedException e1) {
							// e1.printStackTrace();
						}

						try {
							socket.ws = socket.ws.recreate().connect();
						} catch (WebSocketException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
		}
		reconnection.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		reconnection.setDaemon(true);
		reconnection.start();
	}
}
