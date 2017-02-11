package io.disc.discloader.objects.structures;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.objects.gateway.ChannelJSON;

public class PrivateChannel extends TextChannel {

	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		this.type = "dm";
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

	}
}
