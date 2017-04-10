package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.network.json.ChannelJSON;

public class PrivateChannelFactory {

	public IPrivateChannel buildChannel(ChannelJSON data) {
		return new PrivateChannel(DiscLoader.getDiscLoader(), data);
	}
}
