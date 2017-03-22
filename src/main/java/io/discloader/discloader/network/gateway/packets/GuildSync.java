package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.network.gateway.DiscSocket;

public class GuildSync extends DLPacket {

	public GuildSync(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		System.out.println(d);
		// GuildSyncJSON data = gson.fromJson(d, GuildSyncJSON.class);

	}

}
