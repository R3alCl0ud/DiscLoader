package io.disc.discloader.socket.packets;

import com.google.gson.Gson;

import io.disc.discloader.objects.gateway.GuildJSON;
import io.disc.discloader.objects.structures.Guild;
import io.disc.discloader.socket.DiscSocket;

public class GuildCreate extends DiscPacket {

	public GuildCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		String d = gson.toJson(packet.d);
		GuildJSON data = gson.fromJson(d, GuildJSON.class);
		Guild guild = null;
		if (this.socket.loader.guilds.containsKey(data.id))
			guild = this.socket.loader.guilds.get(data.id);
		if (guild != null) {
			if (!guild.available && !data.unavailable) {
				guild.setup(data);
				this.socket.loader.checkReady();
			}
		} else {
			// a brand new guild
			this.socket.loader.addGuild(data);
		}

	}

}
