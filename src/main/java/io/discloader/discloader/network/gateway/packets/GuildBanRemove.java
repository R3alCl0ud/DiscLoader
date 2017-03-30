/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.GuildBanRemoveEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class GuildBanRemove extends AbstractHandler {

	public GuildBanRemove(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = this.loader.guilds.get(data.guild_id);
		IUser user = this.loader.users.get(data.user.id);
		if (user == null) {
			user = this.loader.addUser(data.user);
		}
		GuildBanRemoveEvent event = new GuildBanRemoveEvent(guild, user);
		this.loader.emit(DLUtil.Events.GUILD_BAN_REMOVE, event);
		for (IEventListener e : loader.handlers) {
			e.GuildBanRemove(event);
		}
	}
	
}
