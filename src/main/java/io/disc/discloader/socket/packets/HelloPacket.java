package io.disc.discloader.socket.packets;

import io.disc.discloader.objects.gateway.Hello;
import io.disc.discloader.socket.DiscSocket;

public class HelloPacket extends DiscPacket {

	public HelloPacket(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		Hello hello = this.socket.gson.fromJson(this.socket.gson.toJson(packet.d), Hello.class);
		this.socket.keepAlive(hello.heartbeat_interval);
	}

}
