/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MemberJSON;

/**
 * @author Perry Berman
 *
 */
public class GuildMemberAdd extends AbstractHandler {

	public GuildMemberAdd(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet);
		MemberJSON data = this.gson.fromJson(d, MemberJSON.class);
		IGuild guild = this.loader.guilds.get(data.guild_id);
		guild.addMember(data);
	}

}
