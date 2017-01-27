package io.disc.DiscLoader.socket.packets;

import com.google.gson.Gson;

import io.disc.DiscLoader.objects.gateway.*;
import io.disc.DiscLoader.socket.DiscSocket;

public class GuildCreate extends DiscPacket {

	public GuildCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		System.out.println("Loading guild");
		String d = gson.toJson(packet.d);
		GuildJSON guild = gson.fromJson(d, GuildJSON.class);
		System.out.println(guild.id);
		this.socket.loader.addGuild(guild);
	}

}
