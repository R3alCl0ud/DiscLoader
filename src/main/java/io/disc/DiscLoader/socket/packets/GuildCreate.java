package io.disc.DiscLoader.socket.packets;

import com.google.gson.Gson;

import io.disc.DiscLoader.objects.gateway.GuildGateway;
import io.disc.DiscLoader.socket.DiscSocket;

public class GuildCreate extends DiscPacket {

	public GuildCreate(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		GuildGateway guild = gson.fromJson(gson.toJson(packet.d), GuildGateway.class);
		this.socket.loader.addGuild(guild);
	}

}
