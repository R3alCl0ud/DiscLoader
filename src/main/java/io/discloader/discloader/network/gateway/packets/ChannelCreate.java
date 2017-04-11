/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.channel.ChannelCreateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.Events;

/**
 * @author Perry Berman
 */
public class ChannelCreate extends AbstractHandler {

	public ChannelCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		ChannelJSON data = gson.fromJson(d, ChannelJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IChannel channel = EntityRegistry.addChannel(data, guild);
		ChannelCreateEvent event = new ChannelCreateEvent(channel);
		loader.emit(Events.CHANNEL_CREATE, event);
		loader.emit(event);
	}
}
