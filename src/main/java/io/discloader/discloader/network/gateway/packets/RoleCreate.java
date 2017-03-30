package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.discovery.Mod.EventHandler;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildRoleJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 * @since 0.0.2
 * @see Role
 * @see Guild
 * @see EventHandler
 * @see IEventListener
 */
public class RoleCreate extends AbstractHandler{

	public RoleCreate(DiscSocket socket) {
		super(socket);
	}
	
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildRoleJSON data = this.gson.fromJson(d, GuildRoleJSON.class);
		Guild guild = this.loader.guilds.get(data.guild_id);
		Role role = guild.addRole(data.role);
		GuildRoleCreateEvent event = new GuildRoleCreateEvent(role);
		this.loader.emit(DLUtil.Events.GUILD_ROLE_CREATE, event);
		for (IEventListener e : loader.handlers) {
			e.GuildRoleCreate(event);
		}
	}
}
