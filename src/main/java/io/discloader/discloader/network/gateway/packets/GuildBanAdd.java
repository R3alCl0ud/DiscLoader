package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class GuildBanAdd extends AbstractHandler {

	public GuildBanAdd(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IUser user = EntityRegistry.addUser(data.user);
		IGuildMember member = EntityBuilder.getGuildFactory().buildMember(guild, user);
		guild.removeMember(member);
		GuildBanAddEvent event = new GuildBanAddEvent(member);
		loader.emit(DLUtil.Events.GUILD_BAN_ADD, event);
		loader.emit(event);
	}

}
