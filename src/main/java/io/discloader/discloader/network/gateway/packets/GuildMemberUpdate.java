package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.FactoryManager;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MemberJSON;

/**
 * @author Perry Berman
 */
public class GuildMemberUpdate extends AbstractHandler {

	private GuildFactory gfac = FactoryManager.instance.getGuildFactory();

	public GuildMemberUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		MemberJSON data = this.gson.fromJson(d, MemberJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IGuildMember member = guild.getMember(data.user.id);
		if (member == null) member = gfac.buildMember(guild, data);
		IGuildMember oldMember = gfac.buildMember(member);
		if (shouldEmit()) {
			GuildMemberUpdateEvent event = new GuildMemberUpdateEvent(member, oldMember, guild);
			loader.emit(event);
		}
	}
}
