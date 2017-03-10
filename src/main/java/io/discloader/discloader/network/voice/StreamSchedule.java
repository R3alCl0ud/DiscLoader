package io.discloader.discloader.network.voice;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import io.discloader.discloader.entity.voice.IVoiceConnectionListener;
import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 *
 */
public class StreamSchedule extends AudioEventAdapter implements AudioLoadResultHandler, IVoiceConnectionListener {

	public final ArrayList<AudioTrack> tracks;
	private AudioPlayer player;
	protected VoiceConnection connection;
	private boolean b;

	public StreamSchedule(AudioPlayer player, VoiceConnection connection, boolean b) {
		this.player = player;
		this.b = b;
		this.tracks = new ArrayList<>();
		this.connection = connection;
		this.player.addListener(this);
	}

	public void startNext() {
		if (b)
			if (tracks.size() > 0) {
				AudioTrack nextTrack = tracks.get(0);
				player.startTrack(nextTrack, true);
			} else {
				this.connection.getWs().setSpeaking(false);
				connection.provider.close();
			}
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		if (b)
			this.connection.provider.close();
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		this.connection.getWs().setSpeaking(true);
		if (b)
			this.connection.provider.start();
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (b) {
			if (endReason == AudioTrackEndReason.FINISHED || endReason == AudioTrackEndReason.LOAD_FAILED) {
				tracks.remove(track);
				startNext();
			} else {
				startNext();
			}
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		if (b) {
			if (!connection.provider.isOpen()) {
				connection.provider.start();
			}
			this.connection.getWs().setSpeaking(true);
		}
	}

	@Override
	public void loadFailed(FriendlyException arg0) {
		arg0.printStackTrace();
	}

	@Override
	public void noMatches() {
		System.out.println("Nothing......");
		// oh well, looks like old man jenkins got lost again.
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		if (b) {
			for (AudioTrack track : playlist.getTracks()) {
				tracks.add(track);
			}
			for (IVoiceConnectionListener e : connection.listeners) {
				if (e != this)
					e.playlistLoaded(playlist);
			}

			startNext();
		}
	}

	@Override
	public void trackLoaded(AudioTrack track) {
		if (b) {
			tracks.add(track);
			for (IVoiceConnectionListener e : connection.listeners) {
				if (e != this)
					e.trackLoaded(track);
			}
			startNext();
		}
	}

	@Override
	public void disconnected(String reason) {
	}

	@Override
	public void ready() {
	}

}
