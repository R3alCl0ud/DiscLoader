/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.message.GroupMessageUpdateEvent;
import io.discloader.discloader.common.event.message.GuildMessageUpdateEvent;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageUpdateEvent;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;
import io.discloader.discloader.util.DLUtil.Events;

/**
 * @author Perry Berman
 */
public class MessageUpdate extends AbstractHandler {

	public MessageUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		ITextChannel channel = EntityRegistry.getTextChannelByID(data.channel_id);
		if (channel == null) channel = EntityRegistry.getPrivateChannelByID(data.channel_id);
		if (channel == null) return;

		IMessage oldMessage = channel.getMessage(data.id), message = EntityBuilder.getChannelFactory().buildMessage(channel, data);
		MessageUpdateEvent event = new MessageUpdateEvent(message, oldMessage);
		loader.emit(Events.MESSAGE_UPDATE, event);
		loader.emit(event);
		if (message.getGuild() != null) {
			loader.emit(new GuildMessageUpdateEvent(message, oldMessage));
		} else if (channel.getType() == ChannelType.DM) {
			loader.emit(new PrivateMessageUpdateEvent(message, oldMessage));
		} else if (channel.getType() == ChannelType.GROUPDM) {
			loader.emit(new GroupMessageUpdateEvent(message, oldMessage));
		}
	}

}
