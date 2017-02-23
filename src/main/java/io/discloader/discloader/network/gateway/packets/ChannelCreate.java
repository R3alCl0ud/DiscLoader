/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.ChannelCreateEvent;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.ChannelJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class ChannelCreate extends DiscPacket {

	/**
	 * @param socket
	 */
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
		this.socket.loader.emit(Constants.Events.CHANNEL_CREATE, new ChannelCreateEvent(channel));
	}
}
