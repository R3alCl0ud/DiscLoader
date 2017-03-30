/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class RoleUpdate extends AbstractHandler {

	public RoleUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		Packet data = this.gson.fromJson(d, Packet.class);
		IGuild guild = this.loader.guilds.get(data.guild_id);
		IRole oldRole = guild.getRoles().get(data.role.id);
		IRole role = guild.addRole(data.role);
		GuildRoleUpdateEvent event = new GuildRoleUpdateEvent(role, oldRole);
		this.loader.emit(DLUtil.Events.GUILD_ROLE_UPDATE, event);
		for (IEventListener e : loader.handlers) {
			e.GuildRoleUpdate(event);
		}
	}

	class Packet {
		public String guild_id;
		public RoleJSON role;
	}
}
