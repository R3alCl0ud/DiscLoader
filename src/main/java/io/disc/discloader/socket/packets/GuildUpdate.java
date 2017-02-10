package io.disc.discloader.socket.packets;

import io.disc.discloader.socket.DiscSocket;

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
