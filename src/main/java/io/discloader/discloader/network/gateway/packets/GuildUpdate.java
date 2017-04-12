package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.GuildUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class GuildUpdate extends AbstractHandler {

	public GuildUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildJSON data = gson.fromJson(d, GuildJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.id);
		guild.setup(data);
		GuildUpdateEvent event = new GuildUpdateEvent(guild);
		loader.emit(DLUtil.Events.GUILD_UPDATE, event);
		loader.emit(event);
	}

}
