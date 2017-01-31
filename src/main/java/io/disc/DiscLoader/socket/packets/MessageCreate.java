/**
 * 
 */
package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.objects.gateway.MessageJSON;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Message;
import io.disc.DiscLoader.socket.DiscSocket;

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
		MessageJSON messageJSON = this.gson.fromJson(gson.toJson(packet.d), MessageJSON.class);
		Channel channel = this.socket.loader.channels.get(messageJSON.channel_id);
		channel.messages.put(messageJSON.id, new Message(this.socket.loader, channel, messageJSON));
	}

}
