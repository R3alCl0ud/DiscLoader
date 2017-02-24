package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.GuildRoleDeleteEvent;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.Role;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildRoleJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class RoleDelete extends DLPacket {

	public RoleDelete(DiscSocket socket) {
		super(socket);
	}
	
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildRoleJSON data = this.gson.fromJson(d, GuildRoleJSON.class);
		Guild guild = this.loader.guilds.get(data.guild_id);
		Role role = guild.roles.remove(data.role_id);
		GuildRoleDeleteEvent event = new GuildRoleDeleteEvent(role);
		this.loader.emit(Constants.Events.GUILD_ROLE_DELETE, event);
		for (IEventListener e : DiscLoader.handlers.values()) {
			e.GuildRoleDelete(event);
		}
	}


}
