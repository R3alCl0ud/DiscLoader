package io.discloader.discloader.network.voice;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.gateway.packets.SocketPacket;
import io.discloader.discloader.network.voice.payloads.SessionDescription;
import io.discloader.discloader.network.voice.payloads.VoiceIdentify;
import io.discloader.discloader.network.voice.payloads.VoicePacket;
import io.discloader.discloader.network.voice.payloads.VoiceReady;

/**
 * @author Perry Berman
 * @since 0.0.3
 */
public class VoiceWebSocket extends WebSocketAdapter {

    // VoiceConnection OP codes
    public static final int IDENTIFY = 0;
    public static final int SELECT_PROTOCAL = 1;
    public static final int READY = 2;
    public static final int HEARTBEAT = 3;
    public static final int SESSION_DESCRIPTION = 4;
    public static final int SPEAKING = 5;

    // private HttpHost proxy;

    private WebSocket ws;

    private VoiceConnection connection;

    public int[] secretKey;

    private int sequence = 0;

    @SuppressWarnings("unused")
    private String mode;

    protected Gson gson;

    protected Timer timer = new Timer();

    public VoiceWebSocket(VoiceConnection connection) {
        this.connection = connection;
        this.gson = new Gson();
    }

    public void connect(String gateway, String token) throws IOException, WebSocketException {
        gateway = "wss://" + gateway;
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(15000);
        this.ws = factory.createSocket(gateway).addListener(this);
        this.ws.connect();
    }

    public void disconnect() {
        if (ws != null && ws.isOpen())
            ws.disconnect();
    }

    public int getSequence() {
        return this.sequence;
    }

    public byte[] getSecretKey() {
        byte[] secret = new byte[secretKey.length];
        for (int key = 0; key < secretKey.length; key++) {
            secret[key] = (byte) secretKey[key];
        }
        return secret;
    }

    public void onConnected(WebSocket ws, Map<String, List<String>> arg1) throws Exception {
        this.sendIdentify();
    }

    public void onDisconnected(WebSocket ws, WebSocketFrame frame, WebSocketFrame frame2, boolean isServer) throws Exception {
        System.err.print(String.format("Reason: %s, Code: %d, isServer: %b", frame.getCloseReason(), frame.getCloseCode(), isServer));
        this.connection.disconnected(frame.getCloseReason());
    }

    public void onTextMessage(WebSocket ws, String text) throws Exception {
        SocketPacket packet = this.gson.fromJson(text, SocketPacket.class);
        switch (packet.op) {
            case READY:
                VoiceReady data = this.gson.fromJson(this.gson.toJson(packet.d), VoiceReady.class);
                this.connection.setSSRC(data.ssrc);
                this.connection.connectUDP(data);
                break;
            case SESSION_DESCRIPTION:
                SessionDescription data2 = this.gson.fromJson(this.gson.toJson(packet.d), SessionDescription.class);
                this.secretKey = data2.secret_key;
                this.connection.provider.createNaCl(this.secretKey);
                this.mode = data2.mode;
                this.connection.getFuture().complete(this.connection);
                this.connection.ready();
                break;
            case SPEAKING:
                break;
        }
    }

    public void send(String payload) {
        this.ws.sendText(payload);
    }

    public void sendIdentify() {
        VoiceIdentify payload = new VoiceIdentify(connection.getGuild().getID(), connection.loader.user.getID(), connection.getSessionID(), connection.getToken());
        VoicePacket packet = new VoicePacket(IDENTIFY, payload);
        this.ws.sendText(this.gson.toJson(packet));
    }

    public void setSequence(int s) {
        if (s > this.sequence)
            this.sequence = s;
    }

    public void setSpeaking(boolean isSpeaking) {
        this.send(this.gson.toJson(new VoicePacket(SPEAKING, new Speaking(isSpeaking, 0))));
    }

    public void startHeartbeat(int interval) {
        TimerTask heartbeat = new TimerTask() {
            @Override
            public void run() {
                if (ws.isOpen() && !connection.getUdpClient().udpSocket.isClosed()) {
                    VoicePacket packet = new VoicePacket(HEARTBEAT, sequence);
                    ws.sendText(gson.toJson(packet));
                } else {
                    System.err.print("Died: ws.isOpen(): " + ws.isOpen() + ", udp.isClosed(): " + connection.getUdpClient().udpSocket.isClosed());
                    timer.cancel();
                }
            }
        };
        this.timer.schedule(heartbeat, interval, interval);
    }

}
