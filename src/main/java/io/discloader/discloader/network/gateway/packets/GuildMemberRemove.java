/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 *
 */
public class GuildMemberRemove extends AbstractHandler {

	public GuildMemberRemove(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		Guild guild = this.loader.guilds.get(data.guild_id);
		GuildMember member = guild.members.remove(data.user.id);
		GuildMemberRemoveEvent event = new GuildMemberRemoveEvent(member);
		if (this.loader.ready && this.socket.status == Status.READY) {
			for (IEventListener e : loader.handlers) {
				e.GuildMemberRemove(event);
			}
		}

	}

}
