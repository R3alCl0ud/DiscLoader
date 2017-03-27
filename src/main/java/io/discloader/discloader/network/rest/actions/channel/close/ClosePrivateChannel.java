package io.discloader.discloader.network.rest.actions.channel.close;

import io.discloader.discloader.entity.channels.impl.PrivateChannel;

public class ClosePrivateChannel extends CloseChannel<PrivateChannel> {

	public ClosePrivateChannel(PrivateChannel channel) {
		super(channel);
	}

	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) super.getChannel();
	}
}
