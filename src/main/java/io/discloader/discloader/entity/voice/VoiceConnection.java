package io.discloader.discloader.entity.voice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.neovisionaries.ws.client.WebSocketException;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.voice.VoiceConnectionEvent;
import io.discloader.discloader.common.event.voice.VoiceConnectionReadyEvent;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceStateUpdate;
import io.discloader.discloader.network.voice.AudioSendHandler;
import io.discloader.discloader.network.voice.UDPVoiceClient;
import io.discloader.discloader.network.voice.VoiceGateway;
import io.discloader.discloader.network.voice.payloads.VoiceReady;

/**
 * @author Perry Berman
 */
public class VoiceConnection {

	private class TrackScheduler extends AudioEventAdapter {

		@Override
		public void onPlayerPause(AudioPlayer player) {
			setSpeaking(false);
			sendHandler.stop();
			for (IVoiceEventListener listener : listeners) {
				listener.paused(player.getPlayingTrack());
			}
		}

		@Override
		public void onPlayerResume(AudioPlayer player) {
			// Player was resumed
			setSpeaking(true);
			sendHandler.sendPackets(udpClient.udpSocket);
			for (IVoiceEventListener listener : listeners) {
				listener.resumed(player.getPlayingTrack());
			}
		}

		@Override
		public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
			logger.info(String.format("The audio track at: %s, has ended with reason: %s", track.getInfo().uri, endReason.toString()));
			sendHandler.stop();
			for (IVoiceEventListener listener : listeners) {
				listener.end(track, endReason);
			}
		}

		@Override
		public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
			/*
			 * An already playing track threw an exception (track end event will
			 * still be received separately)
			 */
			sendHandler.stop();
			logger.severe(exception.toString());
			StackTraceElement[] trace = exception.getStackTrace();
			for (StackTraceElement traceElement : trace)
				logger.severe("\tat " + traceElement);
		}

		@Override
		public void onTrackStart(AudioPlayer player, AudioTrack track) {
			// A track started playing
			setSpeaking(true);
			sendHandler.sendPackets(udpClient.udpSocket);
			for (IVoiceEventListener listener : listeners) {
				listener.started(track);
			}
		}

		@Override
		public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
			// Audio track has been unable to provide us any audio, might want
			// to just start a new track
			if (thresholdMs >= 5000) {
				logger.warning(String.format("The track: %s, has been unable to provide us any audio for %dMs.\nIt's recommended to just start a new track", track.getInfo().title == null ? track.getIdentifier() : track.getInfo().title,
						thresholdMs));
				for (IVoiceEventListener listener : listeners) {
					listener.stuck(track, thresholdMs);
				}
			}
		}
	}

	private IGuildVoiceChannel channel;
	private VoiceGateway ws;
	private UDPVoiceClient udpClient;
	private AudioPlayer player;
	private AudioPlayerManager manager;
	private final Logger logger;

	private List<IVoiceEventListener> listeners;

	private AudioSendHandler sendHandler;
	private String endpoint;
	private String token;

	private int SSRC;
	private boolean speaking;

	// Startup things
	private CompletableFuture<VoiceConnection> future;

	private boolean stateUpdated;
	protected final TrackScheduler trackSchedule;

	public VoiceConnection(IGuildVoiceChannel voiceChannel, CompletableFuture<VoiceConnection> future) {
		channel = voiceChannel;
		this.future = future;
		logger = new DLLogger(getGuild() == null ? "VoiceConnection - Channel: " + channel.getID() : "VoiceConnection - Guild: " + getGuild().getID()).getLogger();
		manager = new DefaultAudioPlayerManager();
		udpClient = new UDPVoiceClient();
		ws = new VoiceGateway(this);
		AudioSourceManagers.registerLocalSource(manager);
		AudioSourceManagers.registerRemoteSources(manager);
		player = manager.createPlayer();
		player.addListener(trackSchedule = new TrackScheduler());
		sendHandler = new AudioSendHandler(player, this);
		sendStateUpdate(channel);
		listeners = new ArrayList<>();
	}

	public void connectUDP(VoiceReady data) {

		InetSocketAddress externalAddress = null;
		int tries = 0;
		try {
			InetSocketAddress gg = new InetSocketAddress(endpoint + "", data.port);
			while (externalAddress == null) {
				externalAddress = this.udpClient.discoverAddress(gg, data.ssrc);
				tries++;
				if (externalAddress == null && tries > 5) {
					logger.severe("IP discovery failed!");
					return;
				}
			}
			ws.selectProtocol(externalAddress.getHostString(), externalAddress.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ws.startHeartbeat(data.heartbeat_interval);
	}

	public CompletableFuture<VoiceConnection> disconnect() {
		logger.info("Attempting to disconnect the VoiceConnection");
		player.destroy();
		manager.shutdown();
		sendStateUpdate(null);
		udpClient.udpSocket.close();
		return ws.disconnect();
	}

	public void endpointReceived(String endpoint, String token) {
		this.endpoint = endpoint.substring(0, endpoint.length() - 3);
		this.token = token;
		if (stateUpdated) {
			socketReady();
		}
	}

	public Future<Void> findTrackOrTracks(String id, AudioLoadResultHandler loadHandler) {
		return manager.loadItem(id, loadHandler);
	}

	public void fireEvent(VoiceConnectionEvent event) {
		if (event instanceof VoiceConnectionReadyEvent) {
			future.complete(this);
		}
	}

	public IGuildVoiceChannel getChannel() {
		return channel;
	}

	public IGuild getGuild() {
		if (channel instanceof IGuildVoiceChannel) return ((IGuildVoiceChannel) channel).getGuild();
		return null;
	}

	public DiscLoader getLoader() {
		return channel.getLoader();
	}

	/**
	 * @return the sendHandler
	 */
	public AudioSendHandler getSendHandler() {
		return sendHandler;
	}

	/**
	 * @return the sSRC
	 */
	public int getSSRC() {
		return SSRC;
	}

	public String getToken() {
		return token;
	}

	/**
	 * @return the udpClient
	 */
	public UDPVoiceClient getUDPClient() {
		return udpClient;
	}

	public int getVolume() {
		return player.getVolume();
	}

	/**
	 * @return the ws
	 */
	public VoiceGateway getWebSocket() {
		return ws;
	}

	public boolean isPaused() {
		return player.isPaused();
	}

	/**
	 * @return the speaking
	 */
	public boolean isSpeaking() {
		return speaking;
	}

	/**
	 * @return the stateUpdated
	 */
	public boolean isStateUpdated() {
		return stateUpdated;
	}

	public void pause() {
		player.setPaused(true);
	}

	public void play(AudioTrack track) {
		player.playTrack(track);
	}

	public void play(String track) {
		findTrackOrTracks(track, new AudioLoadResultHandler() {

			@Override
			public void loadFailed(FriendlyException exception) {
				exception.printStackTrace();
			}

			@Override
			public void noMatches() {
				for (IVoiceEventListener listener : listeners) {
					listener.noMatches();
				}
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				for (IVoiceEventListener listener : listeners) {
					listener.playlistLoaded(playlist);
				}
				// player.addListener(new AudioEventAdapter() {
				//
				// private int index = 0;
				//
				// public void onTrackEnd(AudioPlayer player, AudioTrack track,
				// AudioTrackEndReason endReason) {
				// if (endReason.mayStartNext &&
				// !endReason.equals(AudioTrackEndReason.REPLACED)) {
				// index++;
				// if (index < playlist.getTracks().size())
				// play(playlist.getTracks().get(index));
				// }
				// }
				// });
				// play(playlist.getTracks().get(0));
			}

			@Override
			public void trackLoaded(AudioTrack track) {
				for (IVoiceEventListener listener : listeners) {
					listener.trackLoaded(track);
				}
				// logger.info(String.format("Starting track: %s",
				// track.getInfo().title));
				// play(track);
			}

		});
	}

	public void resume() {
		player.setPaused(false);
	}

	private void sendStateUpdate(IVoiceChannel channel) {
		VoiceStateUpdate d = new VoiceStateUpdate(getGuild(), channel, false, false);
		getLoader().socket.send(new Packet(4, d));
	}

	/**
	 * @param sendHandler the sendHandler to set
	 */
	public void setSendHandler(AudioSendHandler sendHandler) {
		this.sendHandler = sendHandler;
	}

	public void setSessionID(String sessionID) {
		ws.sessionID = sessionID;
	}

	/**
	 * @param speaking the speaking to set
	 */
	public void setSpeaking(boolean speaking) {
		this.speaking = speaking;
		ws.setSpeaking(speaking);
	}

	/**
	 * @param ssrc the sSRC to set
	 */
	public void setSSRC(int ssrc) {
		SSRC = ssrc;
	}

	/**
	 * @param stateUpdated the stateUpdated to set
	 */
	public void setStateUpdated(boolean stateUpdated) {
		this.stateUpdated = stateUpdated;
	}

	public void setVolume(int volume) {
		player.setVolume(volume);
	}

	private void socketReady() {
		try {
			ws.connect(endpoint, token);
		} catch (WebSocketException | IOException e) {
			e.printStackTrace();
		}
	}

	public AudioTrack getPlayingTrack() {
		return player.getPlayingTrack();
	}

	/**
	 * @return the listeners
	 */
	public List<IVoiceEventListener> getListeners() {
		return listeners;
	}

	/**
	 * @param listeners the listener(s) to add
	 */
	public void addListener(IVoiceEventListener... listeners) {
		for (IVoiceEventListener listener : listeners) {
			this.listeners.add(listener);
			if (listener instanceof AudioEventAdapter) player.addListener((AudioEventAdapter) listener);
		}
	}
}
