package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.channel.ChannelDeleteEvent;
import io.discloader.discloader.entity.channels.impl.Channel;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.Events;

/**
 * @author Perry Berman
 */
public class ChannelDelete extends AbstractHandler {

	public ChannelDelete(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		ChannelJSON data = gson.fromJson(d, ChannelJSON.class);
		Guild guild = null;
		Channel channel = null;
		if (data.guild_id != null) {
			guild = loader.guilds.get(data.guild_id);
			channel = loader.addChannel(data, guild);
		} else {
			channel = loader.addChannel(data);
		}
		switch (channel.getType()) {
		case TEXT:
			guild.textChannels.remove(channel.getID());
			break;
		case VOICE:
			guild.voiceChannels.remove(channel.getID());
			break;
		default:
			loader.channels.remove(channel.getID());
			break;
		}
		ChannelDeleteEvent event = new ChannelDeleteEvent(channel);
		loader.emit(Events.CHANNEL_DELETE, event);
		loader.emit(event);
	}
}
