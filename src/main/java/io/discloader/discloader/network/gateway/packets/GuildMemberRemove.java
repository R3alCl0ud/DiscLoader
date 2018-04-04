/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildMemberRemoveJSON;
import io.discloader.discloader.util.DLUtil.Events;

/**
 * @author Perry Berman
 */
public class GuildMemberRemove extends AbstractHandler {

	public GuildMemberRemove(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildMemberRemoveJSON data = this.gson.fromJson(d, GuildMemberRemoveJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IGuildMember member = guild.getMember(data.user.id);
		if (member == null)
			member = EntityBuilder.getGuildFactory().buildMember(guild, EntityRegistry.addUser(data.user), new String[] {}, false, false, null);
		member.getUser().setup(data.user);
		guild.removeMember(member);
		if (shouldEmit()) {
			GuildMemberRemoveEvent event = new GuildMemberRemoveEvent(member);
			loader.emit(event);
			loader.emit(Events.GUILD_MEMBER_REMOVE, event);
		}

	}

}
