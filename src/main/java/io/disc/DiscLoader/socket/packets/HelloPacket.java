package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.objects.gateway.Hello;
import io.disc.DiscLoader.socket.DiscSocket;

public class HelloPacket extends DiscPacket {

	public HelloPacket(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		Hello hello  = this.socket.gson.fromJson(this.socket.gson.toJson(packet.d), Hello.class);
		this.socket.keepAlive(hello.heartbeat_interval);
	}

}
