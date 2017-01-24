package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.objects.gateway.Ready;
import io.disc.DiscLoader.socket.DiscSocket;

public class ReadyPacket extends DiscPacket {
	public ReadyPacket(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		packet.d = this.socket.gson.fromJson((String) packet.d, Ready.class);
		System.out.println("Got Ready Packet");
		this.socket.sendHeartbeat(false);
	}
}
