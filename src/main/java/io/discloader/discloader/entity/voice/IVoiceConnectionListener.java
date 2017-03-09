/**
 * 
 */
package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * @author Perry Berman
 *
 */
public interface IVoiceConnectionListener {
	
	/**
	 * Executed when the voice connection gets disconnected
	 * @param reason The reason the voice connection was disconnected
	 */
	void disconnected(String reason);
	
	/**
	 * Executed when the connect is ready to stream
	 */
	void ready();
	
	void trackLoaded(AudioTrack track);
	
	void playlistLoaded(AudioPlaylist playlist);
	
}
