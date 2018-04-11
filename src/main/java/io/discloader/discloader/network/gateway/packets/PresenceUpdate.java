package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.UserUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAvailableEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.Gateway;
import io.discloader.discloader.network.json.PresenceJSON;

/**
 * @author Perry Berman
 */
public class PresenceUpdate extends AbstractHandler {

	public PresenceUpdate(Gateway socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		PresenceJSON data = this.gson.fromJson(gson.toJson(packet.d), PresenceJSON.class);
		IUser user = EntityRegistry.getUserByID(data.user.id);
		if (user == null) {
			if (data.user.username != null) {
				user = EntityRegistry.addUser(data.user);
			} else {
				return;
			}
		}

		IUser oldUser = new User(user);
		user.setup(data.user);
		if (!user.equals(oldUser)) {
			UserUpdateEvent event = new UserUpdateEvent(user, oldUser);
			loader.emit(event);
		}

		IGuild guild = data.guild_id == null ? null : EntityRegistry.getGuildByID(data.guild_id);
		if (guild != null) {
			IGuildMember oldMember = guild.getMember(user.getID()), member;
			if (oldMember == null && !data.status.equalsIgnoreCase("offline")) {
				member = guild.addMember(user, data.roles, false, false, data.nick, false);
				loader.emit(new GuildMemberAvailableEvent(member));
			} else if (oldMember != null) {
				member = guild.addMember(user, data.roles, oldMember.isDeafened(), oldMember.isMuted(), data.nick, false);
				guild.setPresence(data);
				loader.emit(new GuildMemberUpdateEvent(member, oldMember));
			} else {
				guild.setPresence(data);
			}
		}
	}

}
