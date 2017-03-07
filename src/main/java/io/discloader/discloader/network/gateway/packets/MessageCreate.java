/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * @author Perry Berman
 *
 */
public class MessageCreate extends DLPacket {

	public MessageCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		// System.out.print("Gets here?");
		try {
			ITextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
			if (channel == null)
				channel = this.socket.loader.privateChannels.get(data.channel_id);
			Message message = new Message(channel, data);
			channel.getMessages().put(message.id, message);
			MessageCreateEvent event = new MessageCreateEvent(message);
			if (channel.getType() == ChannelType.TEXT) {
				this.socket.loader.emit(DLUtil.Events.MESSAGE_CREATE, event);
				for (IEventListener e : DiscLoader.handlers.values()) {
					e.MessageCreate(event);
				}
			} else {
				this.loader.emit(DLUtil.Events.PRIVATE_MESSAGE_CREATE, event);
				for (IEventListener e : DiscLoader.handlers.values()) {
					e.PrivateMessageCreate(event);
				}
			}
			CommandHandler.handleMessageCreate(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
