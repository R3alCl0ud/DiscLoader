package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;

public class GuildChannelUpdateEvent extends ChannelUpdateEvent {

	public GuildChannelUpdateEvent(IGuildChannel channel, IGuildChannel oldChannel) {
		super(channel, oldChannel);
	}

	@Override
	public IGuildChannel getChannel() {
		return (IGuildChannel) channel;
	}

	@Override
	public IGuildChannel getOldChannel() {
		return (IGuildChannel) oldChannel;
	}

	public IGuild getGuild() {
		return ((IGuildChannel) getChannel()).getGuild();
	}

}
