/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class MessageUpdate extends AbstractHandler {

	public MessageUpdate(DiscSocket socket) {
		super(socket);
	}

	public void handler(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		ITextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		Message oldMessage = channel.getMessage(data.id),
				message = channel.getMessages().put(data.id, new Message(channel, data));
		MessageUpdateEvent event = new MessageUpdateEvent(message, oldMessage);
		this.socket.loader.emit(DLUtil.Events.MESSAGE_UPDATE, event);
		for (IEventListener e : loader.handlers) {
			e.MessageUpdate(event);
		}
	}

}
