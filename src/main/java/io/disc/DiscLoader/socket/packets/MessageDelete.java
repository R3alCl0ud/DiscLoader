package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.objects.gateway.MessageJSON;
import io.disc.DiscLoader.objects.structures.Message;
import io.disc.DiscLoader.objects.structures.TextChannel;
import io.disc.DiscLoader.socket.DiscSocket;
import io.disc.DiscLoader.util.constants;

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
	
	public void handle(SocketPacket packet) {
		MessageJSON data = this.gson.fromJson(this.gson.toJson(packet.d), MessageJSON.class);
		TextChannel channel = this.socket.loader.textChannels.get(data.channel_id);
		if (channel == null) channel = this.socket.loader.privateChannels.get(data.channel_id);
		this.socket.loader.emit(constants.Events.MESSAGE_DELETE, new Message(channel, data));
	}
	
}
