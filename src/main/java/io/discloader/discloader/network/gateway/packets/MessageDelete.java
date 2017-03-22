package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class MessageDelete extends DLPacket {

	public MessageDelete(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(this.gson.toJson(packet.d), MessageJSON.class);
		ITextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		
		MessageDeleteEvent event = new MessageDeleteEvent(new Message(channel, data));
		this.socket.loader.emit(DLUtil.Events.MESSAGE_DELETE, event);
		for (IEventListener e : loader.handlers) {
			e.MessageDelete(event);
		}
	}

}
