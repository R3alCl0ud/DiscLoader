package io.disc.DiscLoader.socket.packets;

import io.disc.DiscLoader.events.UserUpdateEvent;
import io.disc.DiscLoader.objects.gateway.PresenceJSON;
import io.disc.DiscLoader.objects.structures.Guild;
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
			if (guild.presences.containsKey(user.id)) {
				guild.presences.get(user.id).update(data);
			} else if (data.status != "offline") {
				guild.setPresence(data);
			}
		}
	}

}
