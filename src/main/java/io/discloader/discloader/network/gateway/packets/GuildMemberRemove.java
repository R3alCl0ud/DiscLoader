/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.GuildMemberRemoveEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.Constants.Status;

/**
 * @author Perry Berman
 *
 */
public class GuildMemberRemove extends DLPacket {

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
			for (IEventListener e : DiscLoader.handlers.values()) {
				e.GuildMemberRemove(event);
			}
		}

	}

}
