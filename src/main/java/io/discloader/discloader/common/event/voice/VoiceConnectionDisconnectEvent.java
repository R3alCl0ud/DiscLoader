package io.discloader.discloader.common.event.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 *
 */
public class VoiceConnectionDisconnectEvent extends VoiceConnectionEvent {
	
	/**
	 * @param connection
	 */
	public VoiceConnectionDisconnectEvent(VoiceConnection connection) {
		super(connection);
	}
	
}
