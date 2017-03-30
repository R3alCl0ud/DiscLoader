package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.UserUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class PresenceUpdate extends AbstractHandler {
	
	public PresenceUpdate(DiscSocket socket) {
		super(socket);
	}
	
	@Override
	public void handle(SocketPacket packet) {
		PresenceJSON data = this.gson.fromJson(gson.toJson(packet.d), PresenceJSON.class);
		IUser user = loader.users.containsKey(data.user.id) ? loader.users.get(data.user.id) : null;
		if (user == null) {
			if (data.user.username != null) {
				user = loader.addUser(data.user);
			} else {
				return;
			}
		}
		
		IUser oldUser = new User(user);
		user.setup(data.user);
		if (!user.equals(oldUser)) {
			UserUpdateEvent event = new UserUpdateEvent(user, oldUser);
			loader.emit(DLUtil.Events.USER_UPDATE, event);
			loader.emit(event);
		}
		
		IGuild guild = data.guild_id != null ? loader.guilds.get(data.guild_id) : null;
		if (guild != null) {
			IGuildMember oldMember = guild.getMember(user.getID()), member;
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
