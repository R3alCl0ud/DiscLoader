package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.events.GuildMemberUpdateEvent;
import io.disc.DiscLoader.events.UserUpdateEvent;
import io.disc.DiscLoader.objects.gateway.PresenceJSON;
import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildMember;
import io.disc.DiscLoader.objects.structures.Presence;
import io.disc.DiscLoader.objects.structures.User;
import io.disc.DiscLoader.socket.DiscSocket;
import io.disc.DiscLoader.util.constants;
/**
 * @author Perry Berman
 *
 */
public class PresenceUpdate extends DiscPacket {

	/**
	 * @param socket
	 */
	public PresenceUpdate(DiscSocket socket) {
		super(socket);
	}
	
	public void handle(SocketPacket packet) {
		PresenceJSON data = this.gson.fromJson(gson.toJson(packet.d), PresenceJSON.class);
		User user = this.socket.loader.users.containsKey(data.user.id) ? this.socket.loader.users.get(data.user.id) : null;
		if (user == null) {
			if (data.user.username != null) {
				user = this.socket.loader.addUser(data.user);
			} else {
				return;
			}
		}
		
		User oldUser = new User(this.socket.loader, user);
		user.patch(data.user);
		if (!user.equals(oldUser)) {
			this.socket.loader.emit(constants.Events.USER_UPDATE, new UserUpdateEvent(user, oldUser));
		}
		
		Guild guild = data.guild_id != null ? this.socket.loader.guilds.get(data.guild_id) : null;
		if (guild != null) {
			GuildMember member = guild.members.get(user.id);
			if (member == null && !data.status.equalsIgnoreCase("offline")) {
				member = guild.addMember(user, data.roles, false, false, data.nick, false);
				this.socket.loader.emit(constants.Events.GUILD_MEMBER_AVAILABLE, member);
			}
			if (member != null) {
				GuildMember oldMember = new GuildMember(member);
				if (member.getPresence() != null) {
					oldMember.frozenPresence = new Presence(member.getPresence());
				}
				guild.setPresence(data);
				this.socket.loader.emit(constants.Events.PRESENCE_UPDATE, new GuildMemberUpdateEvent(member, oldMember, guild));
			} else {
				guild.setPresence(data);
			}

		}
	}

}
