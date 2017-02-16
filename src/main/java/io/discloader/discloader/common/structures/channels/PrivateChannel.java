package io.discloader.discloader.common.structures.channels;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.gateway.json.ChannelJSON;

public class PrivateChannel extends TextChannel {

	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		this.type = "dm";
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

	}
}
