/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 */
public class GuildMemberRemove extends AbstractHandler {

	public GuildMemberRemove(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = loader.getGuild(data.guild_id);
		IGuildMember member = guild.getMembers().remove(SnowflakeUtil.parse(data.user.id));
		GuildMemberRemoveEvent event = new GuildMemberRemoveEvent(member);
		if (this.loader.ready && this.socket.status == Status.READY) {
			for (IEventListener e : loader.handlers) {
				e.GuildMemberRemove(event);
			}
		}

	}

}
