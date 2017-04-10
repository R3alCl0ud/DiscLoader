package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.GroupChannel;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.network.json.ChannelJSON;

public class GroupChannelFactory {

	public IGroupChannel buildChannel(ChannelJSON data) {
		return new GroupChannel(DiscLoader.getDiscLoader(), data);
	}
}
