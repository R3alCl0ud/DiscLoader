package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil.Events;

/**
 * @author Perry Berman
 */
public class MessageDelete extends AbstractHandler {

	public MessageDelete(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(this.gson.toJson(packet.d), MessageJSON.class);
		long channelID = SnowflakeUtil.parse(data.channel_id);
		ITextChannel channel = EntityRegistry.getTextChannelByID(channelID);
		if (channel == null) channel = EntityRegistry.getPrivateChannelByID(channelID);
		if (channel == null) return;
		IMessage message = channel.getMessage(data.id);
		if (message == null) return;
		MessageDeleteEvent event = new MessageDeleteEvent(message);
		loader.emit(Events.MESSAGE_DELETE, event);
		loader.emit(event);
		if (message.getGuild() != null) {
			loader.emit(new GuildMessageDeleteEvent(message));
		}
	}

}
