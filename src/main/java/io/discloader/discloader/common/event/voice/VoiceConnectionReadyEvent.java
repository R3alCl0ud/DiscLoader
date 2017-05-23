package io.discloader.discloader.common.event.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

public class VoiceConnectionReadyEvent extends VoiceConnectionEvent {

	public VoiceConnectionReadyEvent(VoiceConnection connection) {
		super(connection);
	}

}
