package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.member.GuildMemberEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRoleAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRoleRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.util.DLUtil.Events;

/**
 * @author Perry Berman
 */
public class GuildMemberUpdate extends AbstractHandler {

	private GuildFactory gfac = EntityBuilder.getGuildFactory();

	public GuildMemberUpdate(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		MemberJSON data = this.gson.fromJson(d, MemberJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IGuildMember oldMember = guild.getMember(data.user.id), member = gfac.buildMember(guild, data);
		guild.addMember(member);
		if (shouldEmit() && oldMember != null) {
			GuildMemberUpdateEvent event = new GuildMemberUpdateEvent(member, oldMember);
			loader.emit(Events.GUILD_MEMBER_UPDATE, event);
			loader.emit(event);
			if (!member.getNickname().equals(oldMember.getNickname())) {
				loader.emit(new GuildMemberEvent.NicknameUpdateEvent(member, oldMember.getNickname()));
			}
			member.getRoles().forEach(role -> {
				if (!oldMember.hasRole(role)) {
					loader.emit(new GuildMemberRoleAddEvent(oldMember, member, role));
				}
			});
			oldMember.getRoles().forEach(role -> {
				if (!member.hasRole(role)) {
					loader.emit(new GuildMemberRoleRemoveEvent(oldMember, member, role));
				}
			});
		}
	}
}
