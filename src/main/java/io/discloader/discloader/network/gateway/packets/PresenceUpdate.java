package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.UserUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class PresenceUpdate extends DLPacket {

	public PresenceUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		PresenceJSON data = this.gson.fromJson(gson.toJson(packet.d), PresenceJSON.class);
		User user = loader.users.containsKey(data.user.id) ? loader.users.get(data.user.id) : null;
		if (user == null) {
			if (data.user.username != null) {
				user = loader.addUser(data.user);
			} else {
				return;
			}
		}

		User oldUser = new User(user);
		user.patch(data.user);
		if (!user.equals(oldUser)) {
			UserUpdateEvent event = new UserUpdateEvent(user, oldUser);
			loader.emit(DLUtil.Events.USER_UPDATE, event);
			loader.emit(event);
		}

		Guild guild = data.guild_id != null ? this.socket.loader.guilds.get(data.guild_id) : null;
		if (guild != null) {
			GuildMember oldMember = guild.members.get(user.id), member;
			member = guild.addMember(user, data.roles, false, false, data.nick, false);
			if (oldMember == null && !data.status.equalsIgnoreCase("offline")) {
				loader.emit(DLUtil.Events.GUILD_MEMBER_AVAILABLE, member);
			} else if (oldMember != null) {
				guild.setPresence(data);
				loader.emit(new GuildMemberUpdateEvent(member, oldMember, guild));
			} else {
				guild.setPresence(data);
			}
		}
	}

}
