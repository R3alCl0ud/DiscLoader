package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.network.gateway.DiscSocket;

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
