package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.ChannelGateway;

public class Channel {
	public String id;
	
	
	public Channel(ChannelGateway channel) {
		this.id = channel.id;
	}
}
