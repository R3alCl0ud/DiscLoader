/**
 * 
 */
package io.discloader.discloader.entity.voice;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 * @author Perry Berman
 */
public abstract class VoiceConnectionAdapter implements IVoiceConnectionListener {

	@Override
	public void disconnected(String reason) {
		
	}

	public void playlistLoaded(AudioPlaylist playlist) {

	}

	@Override
	public void ready() {

	}

	public void trackLoaded(AudioTrack track) {

	}

	@Override
	public void started(AudioTrack track) {

	}

}
