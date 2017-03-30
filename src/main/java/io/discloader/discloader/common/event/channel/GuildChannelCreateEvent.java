package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;

public class GuildChannelCreateEvent extends ChannelCreateEvent {
	
	public GuildChannelCreateEvent(GuildChannel channel) {
		super(channel);
	}
	
	public IGuild getGuild() {
		return ((IGuildChannel) getChannel()).getGuild();
	}
	
}
