/**
 * 
 */
package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * @author Perry Berman
 */
public abstract class VoiceEventAdapter implements IVoiceEventListener {

	@Override
	public void disconnected(String reason) {

	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {

	}

	@Override
	public void ready() {

	}

	@Override
	public void trackLoaded(AudioTrack track) {

	}

	@Override
	public void started(AudioTrack track) {

	}

	@Override
	public void noMatches() {
	}

	@Override
	public void loadFailed(FriendlyException exception) {
	}

	@Override
	public void paused(AudioTrack track) {
	}

	@Override
	public void resumed(AudioTrack track) {
	}

	@Override
	public void end(AudioTrack track, AudioTrackEndReason endReason) {
	}

	@Override
	public void stuck(AudioTrack track, long thresholdMs) {
	}

}
