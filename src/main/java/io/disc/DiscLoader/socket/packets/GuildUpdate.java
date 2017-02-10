package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.socket.DiscSocket;

/**
 * @author Perry Berman
 *
 */
public class GuildUpdate extends DiscPacket {

	public GuildUpdate(DiscSocket socket) {
		super(socket);
	}
	
	public void handle(SocketPacket packet) {
		
	}

}
