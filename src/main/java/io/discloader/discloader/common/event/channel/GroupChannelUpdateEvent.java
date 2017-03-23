package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.entity.channels.GuildChannel;
import io.discloader.discloader.entity.guild.Guild;

public class GroupChannelUpdateEvent extends ChannelUpdateEvent {

	public GroupChannelUpdateEvent(GuildChannel channel, GuildChannel oldChannel) {
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
