/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.ChannelUpdateEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class ChannelUpdate extends DLPacket {

	public ChannelUpdate(DiscSocket socket) {
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
		ChannelUpdateEvent event = new ChannelUpdateEvent(channel);
		this.socket.loader.emit(DLUtil.Events.CHANNEL_UPDATE, event);
		for (IEventListener e : DiscLoader.handlers.values()) {
			e.ChannelUpdate(event);
		}
	}


}
