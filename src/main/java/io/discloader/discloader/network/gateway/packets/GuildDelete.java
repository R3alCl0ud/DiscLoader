/**
 * 
 */
package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.GuildDeleteEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class GuildDelete extends DLPacket {

	public GuildDelete(DiscSocket socket) {
		super(socket);
	}
	
	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildJSON data = this.gson.fromJson(d, GuildJSON.class);
		Guild guild = null;
		if (this.socket.loader.guilds.containsKey(data.id)) {
			guild = this.socket.loader.guilds.get(data.id);
			guild.setup(data);
		} else {
			guild = this.socket.loader.addGuild(data);
		}
		if (!guild.available) {
			this.socket.loader.guilds.remove(guild.id);
			if (this.socket.status == Constants.Status.READY && this.socket.loader.ready) {
				GuildDeleteEvent event =  new GuildDeleteEvent(guild);
				this.socket.loader.emit(Constants.Events.GUILD_DELETE, event);
				for (IEventListener e : DiscLoader.handlers.values()) {
					e.GuildDelete(event);
				}
			}
		}
	}
	
}
