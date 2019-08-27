package io.discloader.discloader.core.entity.channel;

import io.discloader.discloader.entity.channel.INewsChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.json.ChannelJSON;

public class NewsChannel extends TextChannel implements INewsChannel {

	public NewsChannel(IGuild guild, ChannelJSON data) {
		super(guild, data);
	}

}
