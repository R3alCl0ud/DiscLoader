package io.discloader.discloader.network.rest.actions.channel.close;

import io.discloader.discloader.entity.channel.IGuildChannel;

public class CloseGuildChannel extends CloseChannel<IGuildChannel> {

	public CloseGuildChannel(IGuildChannel channel) {
		super(channel);
	}
}
