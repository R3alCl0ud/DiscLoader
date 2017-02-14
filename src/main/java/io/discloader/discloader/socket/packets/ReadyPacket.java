package io.discloader.discloader.socket.packets;

import com.google.gson.Gson;

import io.discloader.discloader.objects.gateway.ChannelJSON;
import io.discloader.discloader.objects.gateway.GuildJSON;
import io.discloader.discloader.objects.gateway.Ready;
import io.discloader.discloader.socket.DiscSocket;

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
		this.socket.loader.user = this.socket.loader.addUser(ready.user);
		if (this.socket.loader.user.bot == true)
			this.socket.loader.token = "Bot " + this.socket.loader.token;

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
