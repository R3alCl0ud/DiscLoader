package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildSyncJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;

public class GuildSync extends AbstractHandler {

	public GuildSync(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildSyncJSON data = gson.fromJson(d, GuildSyncJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.id);
		for (PresenceJSON pe : data.presences) {
			guild.setPresence(pe);
		}
		for (MemberJSON me : data.members) {
			guild.addMember(me);
		}

	}

}
