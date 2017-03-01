package io.discloader.discloader.entity.voice;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceStateUpdate;
import io.discloader.discloader.network.voice.StreamProvider;
import io.discloader.discloader.network.voice.UDPVoiceClient;
import io.discloader.discloader.network.voice.VoiceWebSocket;
import io.discloader.discloader.network.voice.payloads.VoiceData;
import io.discloader.discloader.network.voice.payloads.VoicePacket;
import io.discloader.discloader.network.voice.payloads.VoiceReady;
import io.discloader.discloader.network.voice.payloads.VoiceUDPBegin;
import io.discloader.discloader.util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.neovisionaries.ws.client.WebSocketException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class VoiceConnection {

    public AudioPlayer player;

    public final Guild guild;
    public final VoiceChannel channel;
    public final DiscLoader loader;
    public final StreamProvider provider;
    private final CompletableFuture<VoiceConnection> future;
    private final VoiceWebSocket ws;
    private final UDPVoiceClient udpClient;

    private boolean stateUpdated = false;

    /**
     * Voice Server Endpoint.
     */
    private String endpoint;

    /**
     * Voice Server authentication token.
     */
    private String token;

    private String userID;

    private String sessionID;

    private int port;

    private ArrayList<IVoiceConnectionListener> listeners;

    public VoiceConnection(VoiceChannel channel, CompletableFuture<VoiceConnection> future) {
        this.channel = channel;
        this.guild = channel.guild;
        this.loader = channel.loader;
        this.future = future;
        this.udpClient = new UDPVoiceClient(this);
        this.ws = new VoiceWebSocket(this);
        this.provider = new StreamProvider(this);
        this.listeners = new ArrayList<>();
        this.userID = this.loader.user.id;
        this.sendStateUpdate();
    }

    public void addListener(IVoiceConnectionListener listener) {
        this.listeners.add(listener);
    }

    public void sendStateUpdate() {
        VoiceStateUpdate d = new VoiceStateUpdate(this.guild, this.channel, false, false);
        this.loader.socket.send(new Packet(Constants.OPCodes.VOICE_STATE_UPDATE, d));
    }

    public void endpointReceived(String endpoint, String token) {
        this.setEndpoint(endpoint.substring(0, endpoint.length() - 3));
        this.setToken(token);
        if (this.stateUpdated) {
            this.socketReady();
        }
    }

    public void socketReady() {
        try {
            this.ws.connect(this.endpoint, this.token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public void connectUDP(VoiceReady data) {
        InetSocketAddress externalAddress = null;
        int tries = 0;
        while (externalAddress == null) {
            externalAddress = this.udpClient.discoverAddress(new InetSocketAddress(this.endpoint, data.port), data.ssrc);
            tries++;
            if (externalAddress == null && tries > 5) {
                System.err.print("IP discovery failed!");
                return;
            }
        }
        String payload = Constants.gson.toJson(new VoicePacket(1, new VoiceUDPBegin(new VoiceData(externalAddress.getHostString(), externalAddress.getPort()))));
        this.ws.send(payload);
        this.ws.startHeartbeat(data.heartbeat_interval);
    }

    /**
     * The port the voice connection is opened on.
     * 
     * @return the port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * @return the stateUpdated
     */
    public boolean isStateUpdated() {
        return stateUpdated;
    }

    /**
     * @param stateUpdated the stateUpdated to set
     */
    public void setStateUpdated(boolean stateUpdated) {
        this.stateUpdated = stateUpdated;
    }

    /**
     * @return the future
     */
    public CompletableFuture<VoiceConnection> getFuture() {
        return future;
    }

    /**
     * @return the ws
     */
    public VoiceWebSocket getWs() {
        return ws;
    }

    /**
     * @return the sessionID
     */
    public String getSessionID() {
        return this.sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void disconnected(String reason) {
        for (IVoiceConnectionListener e : this.listeners) {
            e.disconnected(reason);
        }
    }

    public void ready() {
        for (IVoiceConnectionListener e : this.listeners) {
            e.ready();
        }
    }

    /**
     * @return the udpClient
     */
    public UDPVoiceClient getUdpClient() {
        return this.udpClient;
    }
    
    public AudioPlayer play(AudioTrack track) {
    	return this.player;
    }
    

}
