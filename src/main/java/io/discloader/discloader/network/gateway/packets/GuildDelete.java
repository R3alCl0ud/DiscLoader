/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.GuildDeleteEvent;
import io.discloader.discloader.common.event.guild.GuildUnavailableEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildJSON;

/**
 * @author Perry Berman
 */
public class GuildDelete extends AbstractHandler {

	public GuildDelete(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildJSON data = this.gson.fromJson(d, GuildJSON.class);
		IGuild guild = null;
		if (EntityRegistry.guildExists(data.id)) {
			guild = EntityRegistry.getGuildByID(data.id);
			guild.setup(data);
		} else {
			guild = EntityRegistry.addGuild(data);
		}
		if (guild.isAvailable()) {
			EntityRegistry.removeGuild(guild);
			if (shouldEmit()) {
				loader.emit(new GuildDeleteEvent(guild));
			}
		} else if (!guild.isAvailable() && shouldEmit()) {
			loader.emit(new GuildUnavailableEvent(guild));
		}
	}

}
