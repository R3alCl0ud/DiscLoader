package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventAdapter;
import io.discloader.discloader.common.event.MessageDeleteEvent;
import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.MessageJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class MessageDelete extends DiscPacket {

	/**
	 * 
	 */
	public MessageDelete(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(this.gson.toJson(packet.d), MessageJSON.class);
		TextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		
		MessageDeleteEvent event = new MessageDeleteEvent(new Message(channel, data));
		this.socket.loader.emit(Constants.Events.MESSAGE_DELETE, event);
		for (IEventAdapter e : DiscLoader.handlers.values()) {
			e.MessageDelete(event);
		}
	}

}
