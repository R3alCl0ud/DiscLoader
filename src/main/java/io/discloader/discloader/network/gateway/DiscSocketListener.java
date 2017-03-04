package io.discloader.discloader.network.gateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.network.gateway.packets.ChannelCreate;
import io.discloader.discloader.network.gateway.packets.ChannelDelete;
import io.discloader.discloader.network.gateway.packets.ChannelUpdate;
import io.discloader.discloader.network.gateway.packets.DLPacket;
import io.discloader.discloader.network.gateway.packets.GuildBanAdd;
import io.discloader.discloader.network.gateway.packets.GuildBanRemove;
import io.discloader.discloader.network.gateway.packets.GuildCreate;
import io.discloader.discloader.network.gateway.packets.GuildDelete;
import io.discloader.discloader.network.gateway.packets.GuildMemberAdd;
import io.discloader.discloader.network.gateway.packets.GuildMemberRemove;
import io.discloader.discloader.network.gateway.packets.GuildMemberUpdate;
import io.discloader.discloader.network.gateway.packets.GuildMembersChunk;
import io.discloader.discloader.network.gateway.packets.GuildUpdate;
import io.discloader.discloader.network.gateway.packets.HelloPacket;
import io.discloader.discloader.network.gateway.packets.MessageCreate;
import io.discloader.discloader.network.gateway.packets.MessageDelete;
import io.discloader.discloader.network.gateway.packets.MessageUpdate;
import io.discloader.discloader.network.gateway.packets.PresenceUpdate;
import io.discloader.discloader.network.gateway.packets.ReadyPacket;
import io.discloader.discloader.network.gateway.packets.RoleCreate;
import io.discloader.discloader.network.gateway.packets.RoleDelete;
import io.discloader.discloader.network.gateway.packets.RoleUpdate;
import io.discloader.discloader.network.gateway.packets.SocketPacket;
import io.discloader.discloader.network.gateway.packets.VoiceServerUpdate;
import io.discloader.discloader.network.gateway.packets.VoiceStateUpdate;
import io.discloader.discloader.util.Constants;
import io.discloader.discloader.util.Constants.OPCodes;
import io.discloader.discloader.util.Constants.Status;
import io.discloader.discloader.util.Constants.WSEvents;

public class DiscSocketListener extends WebSocketAdapter {
    public Gson gson = new Gson();

    public DiscLoader loader;
    public DiscSocket socket;

    private int retries = 0;

    private Thread reconnectionThread = null;

    private final Logger logger = new DLLogger("Gateway Listener").getLogger();

    public HashMap<String, DLPacket> handlers;

    public List<SocketPacket> queue;

    public DiscSocketListener(DiscSocket socket) {
        this.socket = socket;
        this.loader = this.socket.loader;
        this.handlers = new HashMap<String, DLPacket>();
        this.queue = new ArrayList<SocketPacket>();

        this.register(WSEvents.HELLO, new HelloPacket(this.socket));
        this.register(WSEvents.READY, new ReadyPacket(this.socket));
        this.register(WSEvents.GUILD_CREATE, new GuildCreate(this.socket));
        this.register(WSEvents.GUILD_BAN_ADD, new GuildBanAdd(this.socket));
        this.register(WSEvents.GUILD_BAN_REMOVE, new GuildBanRemove(this.socket));
        this.register(WSEvents.GUILD_DELETE, new GuildDelete(this.socket));
        this.register(WSEvents.GUILD_UPDATE, new GuildUpdate(this.socket));
        this.register(WSEvents.GUILD_ROLE_CREATE, new RoleCreate(this.socket));
        this.register(WSEvents.GUILD_ROLE_DELETE, new RoleDelete(this.socket));
        this.register(WSEvents.GUILD_ROLE_UPDATE, new RoleUpdate(this.socket));
        this.register(WSEvents.GUILD_MEMBERS_CHUNK, new GuildMembersChunk(this.socket));
        this.register(WSEvents.GUILD_MEMBER_ADD, new GuildMemberAdd(this.socket));
        this.register(WSEvents.GUILD_MEMBER_REMOVE, new GuildMemberRemove(this.socket));
        this.register(WSEvents.GUILD_MEMBER_UPDATE, new GuildMemberUpdate(this.socket));
        this.register(WSEvents.CHANNEL_CREATE, new ChannelCreate(this.socket));
        this.register(WSEvents.CHANNEL_DELETE, new ChannelDelete(this.socket));
        this.register(WSEvents.CHANNEL_UPDATE, new ChannelUpdate(this.socket));
        this.register(WSEvents.PRESENCE_UPDATE, new PresenceUpdate(this.socket));
        this.register(WSEvents.MESSAGE_CREATE, new MessageCreate(this.socket));
        this.register(WSEvents.MESSAGE_UPDATE, new MessageUpdate(this.socket));
        this.register(WSEvents.MESSAGE_DELETE, new MessageDelete(this.socket));
        this.register(WSEvents.VOICE_STATE_UPDATE, new VoiceStateUpdate(this.socket));
        this.register(WSEvents.VOICE_SERVER_UPDATE, new VoiceServerUpdate(this.socket));
    }

    public void handle(SocketPacket packet) {
        if (packet.op == OPCodes.RECONNECT) {
            this.setSequence(packet.s);
            return;
        }

        if (packet.op == OPCodes.HELLO) {
            this.handlers.get(WSEvents.HELLO).handle(packet);
        }

        if (packet.op == Constants.OPCodes.HEARTBEAT_ACK) {
            this.socket.lastHeartbeatAck = true;
            this.loader.emit("debug", "Heartbeat Acknowledged");
        } else if (packet.op == OPCodes.HEARTBEAT) {
            JSONObject payload = new JSONObject().put("op", OPCodes.HEARTBEAT).put("d", this.socket.s);
            this.socket.send(payload);
            this.loader.emit("debug", "Recieved gateway heartbeat");
        }

        this.setSequence(packet.s);

        if (this.socket.status != Status.READY) {
            if (Constants.EventWhitelist.indexOf(packet.t) == -1) {
                this.queue.add(packet);
                return;
            }
        }

        if (packet.op == OPCodes.DISPATCH) {
            if (!this.handlers.containsKey(packet.t))
                return;
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
        if (socket.status != Status.RECONNECTING) {
            this.socket.lastHeartbeatAck = true;
            this.sendNewIdentify();
        } else {
            
        }
    }

    public void onDisconnected(WebSocket ws, WebSocketFrame frame_1, WebSocketFrame frame_2, boolean isDisconnected) throws Exception {
        this.socket.killHeartbeat();
        if (isDisconnected) {
            logger.severe(String.format("Gateway connection was closed by the server. Close Code: %d, Reason: %s", frame_1.getCloseCode(), frame_1.getCloseReason()));
        } else {
            logger.severe("Client was disconnected from the gateway");
        }
        tryReconnecting();
    }

    @Override
    public void onTextMessage(WebSocket ws, String text) throws Exception {
        this.socket.loader.emit("raw", text);
        for (IEventListener e : DiscLoader.handlers.values()) {
            e.raw(text);
        }
        SocketPacket packet = gson.fromJson(text, SocketPacket.class);
        this.handle(packet);
    }

    public void register(String event, DLPacket handler) {
        this.handlers.put(event, handler);
    }

    public void sendNewIdentify() {
        stopReconnecting();
        JSONObject payload = new JSONObject();
        JSONObject properties = new JSONObject().put("$os", "DiscLoader").put("$browser", "DiscLoader").put("$device", "DiscLoader");
        payload.put("token", this.socket.loader.token).put("large_threshold", 250).put("compress", false).put("properties", properties);

        try {
            if (this.loader.shards > 1) {
                JSONArray te = new JSONArray().put(this.loader.shard).put(this.loader.shards);
                payload.put("shard", te);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        JSONObject packet = new JSONObject();
        packet.put("op", 2).put("d", payload);
        this.socket.send(packet);
        this.socket.s = -1;
    }

    public void setSequence(int s) {
        if (s > this.socket.s)
            this.socket.s = s;
    }

    public void tryReconnecting() {
        this.socket.status = Status.RECONNECTING;
        logger.info("Attempting to reconnect to the gateway");

        reconnectionThread = new Thread("DLResumingThread") {
            public void run() {
                while (socket.status == Status.RECONNECTING && !reconnectionThread.isInterrupted() && retries < 5) {
                    retries++;

                    //                    socket.ws = socket.ws.recreate().connect();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        reconnectionThread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
        reconnectionThread.setDaemon(true);
        reconnectionThread.start();
    }

    public void stopReconnecting() {
        if (reconnectionThread != null) {
            reconnectionThread.interrupt();
            reconnectionThread = null;
        }
    }

}
