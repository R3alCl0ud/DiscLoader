/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.FactoryManager;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;

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
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IGuildMember member = guild.getMember(data.user.id);
		if (member == null) member = FactoryManager.instance.getGuildFactory().buildMember(guild, EntityRegistry.addUser(data.user), new String[] {}, false, false, null);
		guild.removeMember(member);
		if (shouldEmit()) {
			GuildMemberRemoveEvent event = new GuildMemberRemoveEvent(member);
			loader.emit(event);
		}

	}

}
