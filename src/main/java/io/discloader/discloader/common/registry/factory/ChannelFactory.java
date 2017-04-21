package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;

public class ChannelFactory {

	public IChannel buildChannel(ChannelJSON data, IGuild guild) {
		IChannel channel = null;
		if (data.type == DLUtil.ChannelTypes.DM) {
			channel = new PrivateChannel(DiscLoader.getDiscLoader(), data);
		} else if (data.type == DLUtil.ChannelTypes.groupDM) {
			channel = new Channel(DiscLoader.getDiscLoader(), data);
		} else {
			if (guild != null) {
				if (data.type == DLUtil.ChannelTypes.text) {
					channel = new TextChannel(guild, data);
					guild.getTextChannels().put(channel.getID(), (TextChannel) channel);
				} else if (data.type == DLUtil.ChannelTypes.voice) {
					channel = new VoiceChannel(guild, data);
					guild.getVoiceChannels().put(channel.getID(), (VoiceChannel) channel);
				}
			}
		}
		if (channel != null) {
			return channel;
		}
		return new Channel(DiscLoader.getDiscLoader(), data);
	}

	public IMessage buildMessage(ITextChannel channel, MessageJSON data) {
		return new Message<>(channel, data);
	}
}
