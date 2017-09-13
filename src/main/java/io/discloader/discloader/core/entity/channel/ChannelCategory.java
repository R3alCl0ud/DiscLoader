package io.discloader.discloader.core.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.json.ChannelJSON;

public class ChannelCategory extends GuildChannel implements IChannelCategory {

	public ChannelCategory(IGuild guild, ChannelJSON channel) {
		super(guild, channel);
	}

	@Override
	public <T extends IGuildChannel> CompletableFuture<T> addChannel(T guildChannel) {
		return null;
	}

	public void setup(ChannelJSON data) {
		super.setup(data);
	}

}
