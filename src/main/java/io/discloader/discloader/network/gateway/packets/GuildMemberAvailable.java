package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MemberJSON;

public class GuildMemberAvailable extends DLPacket {

	public GuildMemberAvailable(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet);
		MemberJSON data = this.gson.fromJson(d, MemberJSON.class);
	}
	
}
