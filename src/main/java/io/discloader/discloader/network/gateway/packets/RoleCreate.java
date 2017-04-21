package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.discovery.Mod.EventHandler;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
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
public class RoleCreate extends AbstractHandler {

	private GuildFactory gfac = EntityBuilder.getGuildFactory();

	public RoleCreate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildRoleJSON data = gson.fromJson(d, GuildRoleJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IRole role = guild.addRole(gfac.buildRole(guild, data.role));
		GuildRoleCreateEvent event = new GuildRoleCreateEvent(role);
		loader.emit(DLUtil.Events.GUILD_ROLE_CREATE, event);
		loader.emit(event);
	}
}
