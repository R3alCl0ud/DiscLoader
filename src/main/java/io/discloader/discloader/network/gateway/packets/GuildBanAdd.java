package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class GuildBanAdd extends AbstractHandler {
	
	public GuildBanAdd(DiscSocket socket) {
		super(socket);
	}
	
	@Override
	public void handle(SocketPacket packet) {
		String d = gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = loader.guilds.get(data.guild_id);
		IUser user = loader.addUser(data.user);
		IGuildMember member = guild.getMember(data.user.id);
		if (member == null) {
			member = new GuildMember(guild, user);
		}
		GuildBanAddEvent event = new GuildBanAddEvent(member);
		this.loader.emit(DLUtil.Events.GUILD_BAN_ADD, event);
		for (IEventListener e : loader.handlers) {
			e.GuildBanAdd(event);
		}
	}
	
}
