/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.entity.channels.ITextChannel;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.TypingStartJSON;

/**
 * @author Perry Berman
 *
 */
public class TypingStart extends AbstractHandler {

	public TypingStart(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		TypingStartJSON data = gson.fromJson(d, TypingStartJSON.class);
		ITextChannel channel = (ITextChannel) loader.channels.get(data.channel_id);
		User user = loader.users.get(data.user_id);
		channel.getTyping().put(user.id, user);
		loader.emit("");
	}
}
