package io.disc.DiscLoader.socket.packets;

import com.google.gson.Gson;

import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.objects.gateway.Ready;
import io.disc.DiscLoader.socket.DiscSocket;

public class ReadyPacket extends DiscPacket {
	public ReadyPacket(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		String d = gson.toJson(packet.d);
		Ready ready = gson.fromJson(d, Ready.class);
		System.out.println(ready.v);
		this.socket.loader.user = this.socket.loader.addUser(ready.user);
		GuildJSON[] guilds = ready.guilds;
		for (int i = 0; i < guilds.length; i++) {
			this.socket.loader.addGuild(guilds[i]);
		}
		
		this.socket.sendHeartbeat(false);
	}
}
