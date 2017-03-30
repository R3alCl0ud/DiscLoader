package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;

public class GuildChannelDeleteEvent extends ChannelCreateEvent {

	public GuildChannelDeleteEvent(GuildChannel channel) {
		super(channel);
	}

	@Override
	public GuildChannel getChannel() {
		return (GuildChannel) super.getChannel();
	}

	public IGuild getGuild() {
		return ((IGuildChannel) getChannel()).getGuild();
	}

}
