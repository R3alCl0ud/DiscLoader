package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.ChannelJSON;

public class GuildChannel extends Channel {
	public String name;

	public GuildChannel(ChannelJSON channel) {
		super(channel);
		this.name = channel.name;
	}

}
