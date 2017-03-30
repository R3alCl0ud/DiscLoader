package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.core.entity.guild.Guild;

public class GuildChannelCreateEvent extends ChannelCreateEvent {

	public GuildChannelCreateEvent(GuildChannel channel) {
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
