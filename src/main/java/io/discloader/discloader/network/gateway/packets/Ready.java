package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.ReadyJSON;

public class Ready extends AbstractHandler {

	public Ready(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		ReadyJSON readyJSON = gson.fromJson(d, ReadyJSON.class);

		// set session id first just incase some screws up
		socket.sessionID = readyJSON.session_id;

		// setup the Loaders user object
		try {
			loader.user = new DLUser(EntityRegistry.addUser(readyJSON.user));
			if (loader.user.bot) {
				loader.token = "Bot " + loader.token;
			}

			// load the guilds
			for (GuildJSON guild : readyJSON.guilds) {
				EntityRegistry.addGuild(guild);
			}

			// load the private channels
			for (ChannelJSON data : readyJSON.private_channels) {
				EntityRegistry.addChannel(data, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check if the loader is ready to rock & roll
		loader.checkReady();
	}
}
