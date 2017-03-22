/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * @author Perry Berman
 */
public class MessageCreate extends DLPacket {

	public MessageCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		try {
			ITextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
			if (channel == null) channel = this.socket.loader.privateChannels.get(data.channel_id);
			Message message = new Message(channel, data);
			channel.getMessages().put(message.id, message);
			if (channel.isTyping(message.author)) {
				channel.getTyping().remove(message.author.id);
			}
			MessageCreateEvent event = new MessageCreateEvent(message);
			loader.emit(DLUtil.Events.MESSAGE_CREATE, event);
			loader.emit(event);
			if (channel.getType() == ChannelType.TEXT) {
				loader.emit("GuildMessageCreate", (GuildMessageCreateEvent) event);
				loader.emit((GuildMessageCreateEvent) event);
			} else if (channel.getType() == ChannelType.DM) {
				loader.emit(DLUtil.Events.PRIVATE_MESSAGE_CREATE, (PrivateMessageCreateEvent) event);
				loader.emit((PrivateMessageCreateEvent) event);
			}
			CommandHandler.handleMessageCreate(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
