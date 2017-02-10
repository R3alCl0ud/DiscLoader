/**
 * 
 */
package io.disc.discloader.socket.packets;

import io.disc.discloader.objects.gateway.MessageJSON;
import io.disc.discloader.objects.structures.TextChannel;
import io.disc.discloader.socket.DiscSocket;
import io.disc.discloader.util.constants;

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
		this.socket.loader.emit(constants.Events.MESSAGE_UPDATE, channel.messages.get(data.id).patch(data));
	}

}
