package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.ReadyJSON;

public class Ready extends AbstractHandler {

	public Ready(Gateway socket) {
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
			
			/* 
			 * need to make sure that we don't already have the 'Bot ' prefix on the token
			 * otherwise bots fail to work if they've had to start a completely new session after disconnecting
			 */
			if (loader.user.isBot() && !loader.token.startsWith("Bot ")) { 
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
		loader.socket.setRetries(0);
		loader.checkReady();
	}
}
