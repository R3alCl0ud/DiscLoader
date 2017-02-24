/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.GuildRoleUpdateEvent;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.Role;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class RoleUpdate extends DLPacket {

	public RoleUpdate(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		Packet data = this.gson.fromJson(d, Packet.class);
		Guild guild = this.loader.guilds.get(data.guild_id);
		Role role = guild.roles.get(data.role.id);
		Role oldRole = role.clone();
		role.update(data.role);
		GuildRoleUpdateEvent event = new GuildRoleUpdateEvent(role, oldRole);
		this.loader.emit(Constants.Events.GUILD_ROLE_UPDATE, event);
		for (IEventListener e : DiscLoader.handlers.values()) {
			e.GuildRoleUpdate(event);
		}
	}

	class Packet {
		public String guild_id;
		public RoleJSON role;
	}
}
