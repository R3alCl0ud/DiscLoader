package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.network.json.ChannelJSON;

public class ChannelFactory {

	public IChannel buildChannel(ChannelJSON data) {
		IChannel channel = null;
		switch (data.type) {
		case 0:
			channel = new TextChannelFactory().buildChannel(data);
			break;
		case 1:
			channel = new PrivateChannelFactory().buildChannel(data);
			break;
		case 2:
			channel = new VoiceChannelFactory().buildChannel(data);
			break;
		case 3:
			channel = new GroupChannelFactory().buildChannel(data);
			break;
		default:
			channel = new Channel(DiscLoader.getDiscLoader(), data);
			break;
		}
		return channel;
	}
}
