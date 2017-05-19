/**
 * 
 */
package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

/**
 * @author Perry Berman
 */
public interface IVoiceEventListener extends AudioLoadResultHandler {

	/**
	 * Executed when the voice connection gets disconnected
	 * 
	 * @param reason The reason the voice connection was disconnected
	 */
	void disconnected(String reason);

	/**
	 * Executed when the connect is ready to stream
	 */
	void ready();

	void started(AudioTrack track);

	void paused(AudioTrack track);

	void resumed(AudioTrack track);

	void end(AudioTrack track, AudioTrackEndReason endReason);

	void stuck(AudioTrack track, long thresholdMs);
}
