package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.GuildAvailableEvent;
import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildJSON;

public class GuildCreate extends AbstractHandler {

	public GuildCreate(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildJSON data = gson.fromJson(d, GuildJSON.class);
		IGuild guild = null;
		if (EntityRegistry.guildExists(data.id)) {
			guild = EntityRegistry.getGuildByID(data.id);
		}
		if (guild != null) {
			try {
				if (!guild.isAvailable() && !data.unavailable) {
					guild.setup(data);
					if (shouldEmit()) {
						loader.emit(new GuildAvailableEvent(guild));
					}
					loader.checkReady();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else { // a brand new guild
			guild = EntityRegistry.addGuild(data);
			if (shouldEmit()) {
				loader.emit(new GuildCreateEvent(guild));
			}
		}
	}

}
