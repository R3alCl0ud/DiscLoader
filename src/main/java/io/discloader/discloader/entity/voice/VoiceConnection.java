package io.discloader.discloader.entity.voice;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import com.neovisionaries.ws.client.WebSocketException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceStateUpdate;
import io.discloader.discloader.network.voice.StreamProvider;
import io.discloader.discloader.network.voice.StreamSchedule;
import io.discloader.discloader.network.voice.UDPVoiceClient;
import io.discloader.discloader.network.voice.VoiceWebSocket;
import io.discloader.discloader.network.voice.payloads.VoiceData;
import io.discloader.discloader.network.voice.payloads.VoicePacket;
import io.discloader.discloader.network.voice.payloads.VoiceReady;
import io.discloader.discloader.network.voice.payloads.VoiceUDPBegin;
import io.discloader.discloader.util.DLUtil;

/**
 * Represents a connection to a voice channel in Discord.
 * 
 * @author Perry Berman
 */
public class VoiceConnection {

    protected AudioPlayerManager manager;
    protected StreamSchedule trackScheduler;

    /**
     * The connection's AudioPlayer
     */
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

    private int SSRC;

    private ArrayList<IVoiceConnectionListener> listeners;

    private boolean speaking;

    @SuppressWarnings("unused")
    private HashMap<Integer, String> SSRCs;

    public VoiceConnection(VoiceChannel channel, CompletableFuture<VoiceConnection> future) {
        this.channel = channel;
        this.guild = channel.guild;
        this.loader = channel.loader;
        this.future = future;
        this.udpClient = new UDPVoiceClient(this);
        this.ws = new VoiceWebSocket(this);
        this.provider = new StreamProvider(this);
        this.SSRCs = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.userID = this.loader.user.id;

        this.manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(this.manager);
        AudioSourceManagers.registerRemoteSources(this.manager);

        // manager.setOutputHookFactory(outputHookFactory);

        this.player = manager.createPlayer();

        this.trackScheduler = new StreamSchedule(this.player, this, true);

        this.sendStateUpdate();
    }

    /**
     * Something....
     * 
     * @param listener The listener to add
     */
    public void addListener(IVoiceConnectionListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Scary internal stuff
     * 
     * @param data udp information
     */
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
        String payload = DLUtil.gson.toJson(new VoicePacket(1, new VoiceUDPBegin(new VoiceData(externalAddress.getHostString(), externalAddress.getPort()))));
        this.ws.send(payload);
        this.ws.startHeartbeat(data.heartbeat_interval);
    }

    public void disconnected(String reason) {
        for (IVoiceConnectionListener e : this.listeners) {
            e.disconnected(reason);
        }
    }

    /**
     * Scary internal stuff
     * 
     * @param endpoint The voice server to connect to...
     * @param token The token to use for authentication
     */
    public void endpointReceived(String endpoint, String token) {
        this.endpoint = endpoint.substring(0, endpoint.length() - 3);
        this.token = token;
        if (this.stateUpdated) {
            this.socketReady();
        }
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /**
     * @return the future
     */
    public CompletableFuture<VoiceConnection> getFuture() {
        return future;
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
     * @return the sessionID
     */
    public String getSessionID() {
        return this.sessionID;
    }

    /**
     * @return the sSRC
     */
    public int getSSRC() {
        return this.SSRC;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * @return the udpClient
     */
    public UDPVoiceClient getUdpClient() {
        return this.udpClient;
    }

    /**
     * @return the userID
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * @return the ws
     */
    public VoiceWebSocket getWs() {
        return ws;
    }

    /**
     * 
     * @return true if the client is speaking, false otherwise
     */
    public boolean isSpeaking() {
        return this.speaking;
    }

    /**
     * @return the stateUpdated
     */
    public boolean isStateUpdated() {
        return this.stateUpdated;
    }

    public AudioPlayer play(AudioTrack track) {
        trackScheduler.trackLoaded(track);
        return this.player;
    }

    public AudioPlayer play(File track) {
        this.manager.loadItem(track.getAbsolutePath(), this.trackScheduler);
        return this.player;
    }

    public AudioPlayer play(String track) {
        this.manager.loadItem(track, this.trackScheduler);
        return this.player;
    }

    /**
     * Executes when the voice connection has finished being setup
     */
    public void ready() {
        for (IVoiceConnectionListener e : this.listeners) {
            e.ready();
        }
    }

    private void sendStateUpdate() {
        VoiceStateUpdate d = new VoiceStateUpdate(this.guild, this.channel, false, false);
        this.loader.socket.send(new Packet(DLUtil.OPCodes.VOICE_STATE_UPDATE, d));
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * 
     * 
     * @param speaking Whether or not to speak
     */
    public void setSpeaking(boolean speaking) {
        this.speaking = speaking;
        this.ws.setSpeaking(speaking);
    }

    public void setSSRC(int SSRC) {
        this.SSRC = SSRC;
    }

    /**
     * @param stateUpdated the stateUpdated to set
     */
    public void setStateUpdated(boolean stateUpdated) {
        this.stateUpdated = stateUpdated;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    private void socketReady() {
        try {
            this.ws.connect(this.endpoint, this.token);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

}
