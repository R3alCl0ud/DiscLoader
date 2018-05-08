/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.common.event.message.GroupMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.MessageJSON;

/**
 * @author Perry Berman
 */
public class MessageCreate extends AbstractHandler {

	public MessageCreate(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		try {
			long channelID = SnowflakeUtil.parse(data.channel_id);
			ITextChannel channel = EntityRegistry.getTextChannelByID(channelID);
			if (channel == null)
				channel = EntityRegistry.getPrivateChannelByID(channelID);
			if (channel == null)
				channel = EntityRegistry.getGroupChannelByID(channelID);
			if (channel == null)
				return;
			IMessage message = EntityBuilder.getChannelFactory().buildMessage(channel, data);
			channel.getMessages().put(message.getID(), message);
			if (channel.isTyping(message.getAuthor())) {
				channel.getTyping().remove(message.getAuthor().getID());
			}
			MessageCreateEvent event = new MessageCreateEvent(message);
			loader.emit(event);
			if (channel.getType() == ChannelTypes.TEXT) {
				loader.emit(event = new GuildMessageCreateEvent(message));
			} else if (channel.getType() == ChannelTypes.DM) {
				loader.emit(event = new PrivateMessageCreateEvent(message));
			} else if (channel.getType() == ChannelTypes.GROUP) {
				loader.emit(event = new GroupMessageCreateEvent(message));
			}
			CommandHandler.handleMessageCreate(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
