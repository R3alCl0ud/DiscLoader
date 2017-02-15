package io.discloader.discloader.objects.structures.channels;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.objects.gateway.ChannelJSON;

public class PrivateChannel extends TextChannel {

	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		this.type = "dm";
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

	}
}
