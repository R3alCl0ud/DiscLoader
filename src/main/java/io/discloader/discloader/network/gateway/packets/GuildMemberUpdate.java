package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.guild.member.GuildMemberEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRoleAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRoleRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.MemberJSON;

/**
 * @author Perry Berman
 */
public class GuildMemberUpdate extends AbstractHandler {

	public GuildMemberUpdate(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		MemberJSON data = this.gson.fromJson(d, MemberJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IGuildMember oldMember = guild.getMember(data.user.id), member = guild.addMember(data);
		if (shouldEmit() && oldMember != null) {
			loader.emit(new GuildMemberUpdateEvent(member, oldMember));
			if ((member.getNickname() != null && oldMember.getNickname() == null) || (member.getNickname() == null && oldMember.getNickname() != null) || ((member.getNickname() != null && oldMember.getNickname() != null) && !member.getNickname().equals(oldMember.getNickname()))) {
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
