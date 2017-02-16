package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.common.structures.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.GuildJSON;

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
