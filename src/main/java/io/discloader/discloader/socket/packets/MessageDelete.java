package io.discloader.discloader.socket.packets;

import io.discloader.discloader.objects.gateway.MessageJSON;
import io.discloader.discloader.objects.structures.Message;
import io.discloader.discloader.objects.structures.channels.TextChannel;
import io.discloader.discloader.socket.DiscSocket;
import io.discloader.discloader.util.constants;

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
		this.socket.loader.emit(constants.Events.MESSAGE_DELETE, new Message(channel, data));
	}

}
