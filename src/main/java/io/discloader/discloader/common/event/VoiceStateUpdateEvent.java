package io.discloader.discloader.common.event;

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
