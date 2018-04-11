package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class GuildBanAdd extends AbstractHandler {

	public GuildBanAdd(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IUser user = EntityRegistry.addUser(data.user);
		user.setup(data.user);
		guild.removeMember(user);
		GuildBanAddEvent event = new GuildBanAddEvent(guild, user);
		loader.emit(DLUtil.Events.GUILD_BAN_ADD, event);
		loader.emit(event);
	}

}
