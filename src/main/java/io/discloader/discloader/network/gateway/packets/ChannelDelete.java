package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.ChannelDeleteEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class ChannelDelete extends DLPacket {

	public ChannelDelete(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		ChannelJSON data = this.gson.fromJson(d, ChannelJSON.class);
		Guild guild = null;
		Channel channel = null;
		if (data.guild_id != null) {
			guild = this.socket.loader.guilds.get(data.guild_id);
			channel = this.socket.loader.addChannel(data, guild);
		} else {
			channel = this.socket.loader.addChannel(data);
		}
		switch (channel.getType()) {
		case TEXT:
			guild.textChannels.remove(channel.id);
			break;
		case VOICE:
			guild.voiceChannels.remove(channel.id);
			break;
		default:
			this.loader.channels.remove(channel.id);
			break;
		}
		ChannelDeleteEvent event = new ChannelDeleteEvent(channel);
		this.socket.loader.emit(DLUtil.Events.CHANNEL_DELETE, event);
		for (IEventListener e : DiscLoader.handlers.values()) {
			e.ChannelDelete(event);
		}
	}
}
