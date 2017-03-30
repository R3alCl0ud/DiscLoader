package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.core.entity.guild.Guild;

public class GuildChannelUpdateEvent extends ChannelUpdateEvent {

	public GuildChannelUpdateEvent(GuildChannel channel, GuildChannel oldChannel) {
		super(channel, oldChannel);
	}

	@Override
	public GuildChannel getChannel() {
		return (GuildChannel) channel;
	}

	@Override
	public GuildChannel getOldChannel() {
		return (GuildChannel) oldChannel;
	}

	public Guild getGuild() {
		return getChannel().guild;
	}

}
