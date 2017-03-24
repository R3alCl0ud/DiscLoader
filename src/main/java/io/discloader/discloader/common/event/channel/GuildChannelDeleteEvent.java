package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.entity.channels.impl.GuildChannel;
import io.discloader.discloader.entity.guild.Guild;

public class GuildChannelDeleteEvent extends ChannelCreateEvent {

	public GuildChannelDeleteEvent(GuildChannel channel) {
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
