/**
 * 
 */
package io.disc.discloader.socket.packets;

import io.disc.discloader.events.MessageCreateEvent;
import io.disc.discloader.objects.gateway.MessageJSON;
import io.disc.discloader.objects.loader.DiscRegistry;
import io.disc.discloader.objects.structures.Message;
import io.disc.discloader.objects.structures.TextChannel;
import io.disc.discloader.socket.DiscSocket;
import io.disc.discloader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class MessageCreate extends DiscPacket {

	/**
	 * @param socket
	 */
	public MessageCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		TextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null)
			channel = this.socket.loader.privateChannels.get(data.channel_id);
		Message message = new Message(channel, data);
		channel.messages.put(message.id, message);
		MessageCreateEvent e = new MessageCreateEvent(message);
		this.socket.loader.emit(constants.Events.MESSAGE_CREATE, e);
		DiscRegistry.executeCommand(e);
	}

}
