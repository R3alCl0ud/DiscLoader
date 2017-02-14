package io.discloader.discloader.socket.packets;

import io.discloader.discloader.socket.DiscSocket;

/**
 * @author Perry Berman
 *
 */
public class GuildUpdate extends DiscPacket {

	public GuildUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {

	}

}
