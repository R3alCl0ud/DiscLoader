package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.network.json.ChannelJSON;

public class VoiceChannelFactory {
	public IVoiceChannel buildChannel(ChannelJSON data) {
		return new VoiceChannel(EntityRegistry.getGuildByID(data.guild_id), data);
	}
}
