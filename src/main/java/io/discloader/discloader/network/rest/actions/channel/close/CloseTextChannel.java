package io.discloader.discloader.network.rest.actions.channel.close;

import io.discloader.discloader.core.entity.channel.TextChannel;

public class CloseTextChannel extends CloseChannel<TextChannel> {

	public CloseTextChannel(TextChannel channel) {
		super(channel);
	}

	@Override
	public TextChannel getChannel() {
		return (TextChannel) super.getChannel();
	}

}
