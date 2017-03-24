package io.discloader.discloader.common.event.voice;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.voice.VoiceState;

public class VoiceStateUpdateEvent extends DLEvent {

	public final VoiceState oldState;
	public final VoiceState currentState;

	public VoiceStateUpdateEvent(VoiceState oldState, VoiceState state) {
		super(state.guild.loader);
		this.oldState = oldState;
		this.currentState = state;
	}

}
