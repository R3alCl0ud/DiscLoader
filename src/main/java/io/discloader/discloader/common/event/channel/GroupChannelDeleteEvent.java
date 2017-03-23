package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.entity.channels.GuildChannel;
import io.discloader.discloader.entity.guild.Guild;

public class GroupChannelDeleteEvent extends ChannelCreateEvent {

	public GroupChannelDeleteEvent(GuildChannel channel) {
		super(channel);
	}

	@Override
	public GuildChannel getChannel() {
		return (GuildChannel) super.getChannel();
	}

	public Guild getGuild() {
		return getChannel().guild;
	}

}
