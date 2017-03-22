package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildSyncJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;

public class GuildSync extends DLPacket {

	public GuildSync(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildSyncJSON data = gson.fromJson(d, GuildSyncJSON.class);
		Guild guild = loader.guilds.get(data.id);
		for (PresenceJSON pe : data.presences) {
			guild.setPresence(pe);
		}
		for (MemberJSON me : data.members) {
			guild.addMember(me);
		}
	}

}
