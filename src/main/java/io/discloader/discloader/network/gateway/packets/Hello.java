package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.HelloJSON;

public class Hello extends AbstractHandler {

	public Hello(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		HelloJSON helloJSON = gson.fromJson(gson.toJson(packet.d), HelloJSON.class);
		this.socket.keepAlive(helloJSON.heartbeat_interval);
	}

}
