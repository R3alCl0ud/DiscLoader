package io.discloader.discloader.network.rest.actions.channel.close;

import io.discloader.discloader.entity.channels.impl.GuildChannel;

public class CloseGuildChannel extends CloseChannel<GuildChannel> {

	public CloseGuildChannel(GuildChannel channel) {
		super(channel);
	}

	@Override
	public GuildChannel getChannel() {
		return (GuildChannel) super.getChannel();
	}

}
