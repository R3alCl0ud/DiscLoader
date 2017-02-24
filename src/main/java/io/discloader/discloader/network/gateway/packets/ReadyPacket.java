package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.entity.DLUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.Ready;

public class ReadyPacket extends DiscPacket {
	public ReadyPacket(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		Gson gson = new Gson();
		String d = gson.toJson(packet.d);
		Ready ready = gson.fromJson(d, Ready.class);

		// send first heartbeat in response to ready packet
		this.socket.sendHeartbeat(false);

		// setup the Loaders user object
		this.socket.loader.user = new DLUser(this.loader.addUser(ready.user));
		if (this.socket.loader.user.bot == true) {
			this.socket.loader.token = "Bot " + this.socket.loader.token;
		}

		// System.out.println(ready.v);
		GuildJSON[] guilds = ready.guilds;
		for (int i = 0; i < guilds.length; i++) {
			this.socket.loader.addGuild(guilds[i]);
		}

		for (ChannelJSON data : ready.private_channels)
			this.socket.loader.addChannel(data);

		this.socket.sessionID = ready.session_id;
		// check if the loader is ready to rock & roll
		this.socket.loader.checkReady();
	}
}
