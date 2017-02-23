package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.gateway.DiscSocket;

/**
 * @author Perry Berman
 *
 */
public abstract class DiscPacket {
	public DiscSocket socket;
	public DiscLoader loader;
	public Gson gson;

	public DiscPacket(DiscSocket socket) {
		this.socket = socket;
		this.loader = this.socket.loader;
		this.gson = new Gson();
	}

	public void handle(SocketPacket packet) {

	}
}
