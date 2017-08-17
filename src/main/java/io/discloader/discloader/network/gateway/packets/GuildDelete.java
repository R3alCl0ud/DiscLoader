/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.GuildDeleteEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.DLUtil.Events;
import io.discloader.discloader.util.DLUtil.Status;

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
		if (!guild.isAvailable()) {
			EntityRegistry.removeGuild(guild);
			if (socket.status == Status.READY && loader.ready) {
				GuildDeleteEvent event = new GuildDeleteEvent(guild);
				loader.emit(Events.GUILD_DELETE, event);
				for (IEventListener e : loader.handlers) {
					e.GuildDelete(event);
				}
			}
		}
	}

}
