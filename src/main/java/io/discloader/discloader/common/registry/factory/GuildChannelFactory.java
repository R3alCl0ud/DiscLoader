package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.channel.GuildChannel;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.network.json.ChannelJSON;

public class GuildChannelFactory {

	public IGuildChannel buildChannel(ChannelJSON data) {
		return new GuildChannel(EntityRegistry.getGuildByID(data.guild_id), data);
	}
}
