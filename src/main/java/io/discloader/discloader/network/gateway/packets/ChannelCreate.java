/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.channel.ChannelCreateEvent;
import io.discloader.discloader.entity.channels.impl.Channel;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class ChannelCreate extends AbstractHandler {

	public ChannelCreate(DiscSocket socket) {
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
		ChannelCreateEvent event = new ChannelCreateEvent(channel);
		this.socket.loader.emit(DLUtil.Events.CHANNEL_CREATE, event);
		for (IEventListener e : loader.handlers) {
			e.ChannelCreate(event);
		}
	}
}
