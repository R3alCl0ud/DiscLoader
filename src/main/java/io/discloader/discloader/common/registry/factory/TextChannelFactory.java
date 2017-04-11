package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.json.ChannelJSON;

public class TextChannelFactory {

	public ITextChannel buildChannel(ChannelJSON data, IGuild guild) {
		return new TextChannel(guild, data);
	}
}
