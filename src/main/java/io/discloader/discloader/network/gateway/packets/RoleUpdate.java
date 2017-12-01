package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class RoleUpdate extends AbstractHandler {

	public RoleUpdate(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		Packet data = this.gson.fromJson(d, Packet.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IRole oldRole = guild.getRoleByID(data.role.id);
		IRole role = guild.addRole(data.role);
		GuildRoleUpdateEvent event = new GuildRoleUpdateEvent(role, oldRole);
		loader.emit(DLUtil.Events.GUILD_ROLE_UPDATE, event);
		loader.emit(event);
	}

	class Packet {
		public String guild_id;
		public RoleJSON role;
	}
}
