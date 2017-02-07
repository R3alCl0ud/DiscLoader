/**
 * 
 */
package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.objects.gateway.MessageJSON;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Message;
import io.disc.DiscLoader.socket.DiscSocket;
import io.disc.DiscLoader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class MessageCreate extends DiscPacket{

	/**
	 * @param socket
	 */
	public MessageCreate(DiscSocket socket) {
		super(socket);
	}
	
	@Override
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		Channel channel = this.socket.loader.channels.get(data.channel_id);
		Message message = new Message(this.socket.loader, channel, data);
		channel.messages.put(message.id, message);			
		this.socket.loader.emit(constants.Events.MESSAGE_CREATE, message);
	}

}
