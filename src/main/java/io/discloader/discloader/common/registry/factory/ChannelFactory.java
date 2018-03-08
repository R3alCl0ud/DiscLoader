package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.ChannelCategory;
import io.discloader.discloader.core.entity.channel.GroupChannel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.MessageJSON;

public class ChannelFactory {

	public IChannel buildChannel(ChannelJSON data, DiscLoader loader, IGuild guild) {
		return buildChannel(data, loader, guild, true);
	}

	public IChannel buildChannel(ChannelJSON data, DiscLoader loader, IGuild guild, boolean insert) {
		IChannel channel = null;
		if (ChannelTypes.fromCode(data.type) == ChannelTypes.DM) {
			channel = new PrivateChannel(loader, data);
		} else if (ChannelTypes.fromCode(data.type) == ChannelTypes.GROUP) {
			channel = new GroupChannel(loader, data);
		} else {
			if (guild != null) {
				if (ChannelTypes.fromCode(data.type) == ChannelTypes.TEXT) {
					channel = new TextChannel(guild, data);
					if (insert)
						guild.getTextChannels().put(channel.getID(), (TextChannel) channel);
				} else if (ChannelTypes.fromCode(data.type) == ChannelTypes.VOICE) {
					channel = new VoiceChannel(guild, data);
					if (insert)
						guild.getVoiceChannels().put(channel.getID(), (VoiceChannel) channel);
				} else if (ChannelTypes.fromCode(data.type) == ChannelTypes.CATEGORY) {
					channel = new ChannelCategory(guild, data);
					if (insert)
						guild.getChannelCategories().put(channel.getID(), (IChannelCategory) channel);
				}
			}
		}
		if (channel != null) {
			return channel;
		}
		return new Channel(loader, data);
	}

	public IMessage buildMessage(ITextChannel channel, MessageJSON data) {
		return new Message<>(channel, data);
	}
}
