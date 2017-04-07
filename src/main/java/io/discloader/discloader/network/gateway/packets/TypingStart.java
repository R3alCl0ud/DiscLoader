/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.TypingStartJSON;

/**
 * @author Perry Berman
 */
public class TypingStart extends AbstractHandler {

	public TypingStart(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		TypingStartJSON data = gson.fromJson(d, TypingStartJSON.class);
		ITextChannel channel = (ITextChannel) loader.channels.get(SnowflakeUtil.parse(data.channel_id));
		IUser user = loader.users.get(SnowflakeUtil.parse(data.user_id));
		channel.getTyping().put(user.getID(), user);

		// loader.emit("");
	}
}
