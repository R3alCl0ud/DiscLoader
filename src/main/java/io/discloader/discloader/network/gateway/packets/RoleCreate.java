package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventAdapter;
import io.discloader.discloader.common.event.GuildRoleCreateEvent;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.Role;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.GuildRoleJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 * @since 0.0.2
 * @see Role
 * @see Guild
 * @see Mod#EventHandler
 * @see IEventAdapter
 */
public class RoleCreate extends DiscPacket{

	public RoleCreate(DiscSocket socket) {
		super(socket);
	}
	
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildRoleJSON data = this.gson.fromJson(d, GuildRoleJSON.class);
		Guild guild = this.loader.guilds.get(data.guild_id);
		Role role = guild.addRole(data.role);
		GuildRoleCreateEvent event = new GuildRoleCreateEvent(role);
		this.loader.emit(Constants.Events.GUILD_ROLE_CREATE, event);
		for (IEventAdapter e : DiscLoader.handlers.values()) {
			e.GuildRoleCreate(event);
		}
	}
}
