package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.channel.ChannelDeleteEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.ChannelJSON;

/**
 * @author Perry Berman
 */
public class ChannelDelete extends AbstractHandler {

	public ChannelDelete(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		ChannelJSON data = gson.fromJson(d, ChannelJSON.class);
		IGuild guild = null;
		IChannel channel = null;
		if (data.guild_id != null) {
			guild = EntityRegistry.getGuildByID(data.guild_id);
			channel = EntityRegistry.addChannel(data, loader, guild);
		} else {
			channel = EntityRegistry.addChannel(data, loader);
		}
		switch (channel.getType()) {
		case TEXT:
			guild.getTextChannels().remove(channel.getID());
			break;
		case VOICE:
			guild.getVoiceChannels().remove(channel.getID());
			break;
		default:
			EntityRegistry.removeChannel(channel);
			break;
		}
		ChannelDeleteEvent event = new ChannelDeleteEvent(channel);
		loader.emit(event);
	}
}
