package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.socket.DiscSocket;

/**
 * @author perryberman
 *
 */
public abstract class DiscPacket {
	public DiscSocket socket;
	
	public DiscPacket(DiscSocket socket) {
		this.socket = socket;
	}
	
	public void handle(SocketPacket packet) {
		
	}
}
