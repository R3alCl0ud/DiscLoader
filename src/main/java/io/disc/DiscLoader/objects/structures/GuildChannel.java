package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.ChannelGateway;

public class GuildChannel extends Channel {
	public String name;

	public GuildChannel(ChannelGateway channel) {
		super(channel);
		this.name = channel.name;
	}

}
