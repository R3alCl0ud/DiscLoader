package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import io.discloader.discloader.common.event.voice.VoiceConnectionDisconnectEvent;
import io.discloader.discloader.common.event.voice.VoiceConnectionReadyEvent;

/**
 * @author Perry Berman
 */
public interface IVoiceEventListener extends AudioLoadResultHandler {

	/**
	 * Executed when the voice connection gets disconnected
	 * 
	 * @param event
	 *            an event object
	 */
	void disconnected(VoiceConnectionDisconnectEvent event);

	/**
	 * Executed when the connect is ready to stream
	 */
	void ready(VoiceConnectionReadyEvent event);

	void started(AudioTrack track);

	void paused(AudioTrack track);

	void resumed(AudioTrack track);

	void end(AudioTrack track, AudioTrackEndReason endReason);

	void stuck(AudioTrack track, long thresholdMs);
}
