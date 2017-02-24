/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.GuildBanAddEvent;
import io.discloader.discloader.common.event.IEventAdapter;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.User;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.gateway.json.GuildBanJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class GuildBanAdd extends DiscPacket {

	/**
	 * @param socket
	 */
	public GuildBanAdd(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildBanJSON data = this.gson.fromJson(d, GuildBanJSON.class);
		Guild guild = this.loader.guilds.get(data.guild_id);
		User user = this.loader.addUser(data.user);
		GuildMember member = guild.members.get(data.user.id);
		if (member == null) {
			member = new GuildMember(guild, user);
		}
		GuildBanAddEvent event = new GuildBanAddEvent(member);
		this.loader.emit(Constants.Events.GUILD_BAN_ADD, event);
		for (IEventAdapter e : DiscLoader.handlers.values()) {
			e.GuildBanAdd(event);
		}
	}

}
