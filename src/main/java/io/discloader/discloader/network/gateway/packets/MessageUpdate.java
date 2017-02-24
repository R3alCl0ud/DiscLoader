/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventAdapter;
import io.discloader.discloader.common.event.MessageUpdateEvent;
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class MessageUpdate extends DiscPacket {

	/**
	 * @param socket
	 */
	public MessageUpdate(DiscSocket socket) {
		super(socket);
	}

	public void handler(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		TextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		
		MessageUpdateEvent event = new MessageUpdateEvent(channel.messages.get(data.id).patch(data));
		this.socket.loader.emit(Constants.Events.MESSAGE_UPDATE, event);
		for (IEventAdapter e : DiscLoader.handlers.values()) {
			e.MessageUpdate(event);
		}
	}

}
