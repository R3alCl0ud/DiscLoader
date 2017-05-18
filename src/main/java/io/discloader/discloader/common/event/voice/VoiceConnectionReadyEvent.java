package io.discloader.discloader.common.event.voice;

import io.discloader.discloader.entity.voice.VoiceConnect;

public class VoiceConnectionReadyEvent extends VoiceConnectionEvent {

	public VoiceConnectionReadyEvent(VoiceConnect connection) {
		super(connection);
	}

}
