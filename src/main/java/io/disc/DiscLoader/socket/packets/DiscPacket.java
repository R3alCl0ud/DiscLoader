package io.disc.DiscLoader.socket.packets;

import com.google.gson.Gson;

import io.disc.DiscLoader.socket.DiscSocket;

/**
 * @author Perry Berman
 *
 */
public abstract class DiscPacket {
	public DiscSocket socket;
	public Gson gson;
	
	public DiscPacket(DiscSocket socket) {
		this.socket = socket;
		this.gson = new Gson();
	}
	
	public void handle(SocketPacket packet) {
		
	}
}
