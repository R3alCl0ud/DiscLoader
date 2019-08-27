package io.discloader.discloader.core.entity.channel;

import io.discloader.discloader.entity.channel.IStoreChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.json.ChannelJSON;

public class StoreChannel extends GuildChannel implements IStoreChannel {

	public StoreChannel(IGuild guild, ChannelJSON channel) {
		super(guild, channel);
	}

}
