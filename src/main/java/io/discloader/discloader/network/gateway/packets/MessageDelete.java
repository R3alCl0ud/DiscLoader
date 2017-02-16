package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.structures.Message;
import io.discloader.discloader.common.structures.channels.TextChannel;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.MessageJSON;
import io.discloader.discloader.util.Constant;

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
		this.socket.loader.emit(Constant.Events.MESSAGE_DELETE, new Message(channel, data));
	}

}
