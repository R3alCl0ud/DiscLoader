package io.discloader.discloader.network.rest.actions.channel.close;

import io.discloader.discloader.entity.channels.impl.VoiceChannel;

public class CloseVoiceChannel extends CloseChannel<VoiceChannel> {

	public CloseVoiceChannel(VoiceChannel channel) {
		super(channel);
	}

	@Override
	public VoiceChannel getChannel() {
		return (VoiceChannel) super.getChannel();
	}

}
