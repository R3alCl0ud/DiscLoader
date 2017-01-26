package io.disc.DiscLoader.socket.packets;

import com.google.gson.Gson;

import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.socket.DiscSocket;

public class GuildCreate extends DiscPacket {

	public GuildCreate(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		GuildJSON guild = gson.fromJson(gson.toJson(packet.d), GuildJSON.class);
		this.socket.loader.addGuild(guild);
	}

}
