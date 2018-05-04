package io.discloader.discloader.network.gateway.packets;

import java.util.HashMap;
import java.util.Map;

import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.GuildMembersChunkJSON;
import io.discloader.discloader.network.json.MemberJSON;

/**
 * @author Perry Berman
 */
public class GuildMembersChunk extends AbstractHandler {

	public GuildMembersChunk(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildMembersChunkJSON data = this.gson.fromJson(d, GuildMembersChunkJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		if (guild == null)
			return;
		Map<Long, IGuildMember> members = new HashMap<>();
		for (MemberJSON m : data.members) {
			IGuildMember member = guild.addMember(m);
			members.put(member.getID(), member);
		}
		GuildMembersChunkEvent event = new GuildMembersChunkEvent(guild, members);
		loader.emit(event);
	}
}
