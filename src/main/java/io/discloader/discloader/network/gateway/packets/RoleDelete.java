package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.role.GuildRoleDeleteEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildRoleJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class RoleDelete extends AbstractHandler {

	public RoleDelete(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildRoleJSON data = this.gson.fromJson(d, GuildRoleJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IRole role = guild.removeRole(data.role_id);
		if (role == null) return;
		GuildRoleDeleteEvent event = new GuildRoleDeleteEvent(role);
		loader.emit(DLUtil.Events.GUILD_ROLE_DELETE, event);
		loader.emit(event);
	}

}
