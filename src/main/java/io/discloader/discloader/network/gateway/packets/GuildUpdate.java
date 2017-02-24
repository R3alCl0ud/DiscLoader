package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.GuildUpdateEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class GuildUpdate extends DLPacket {

	public GuildUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		GuildJSON data = this.gson.fromJson(d, GuildJSON.class);
		Guild guild = this.loader.guilds.get(data.id);
		guild.setup(data);
		GuildUpdateEvent event = new GuildUpdateEvent(guild);
		this.loader.emit(Constants.Events.GUILD_UPDATE, event);
		for (IEventListener e : DiscLoader.handlers.values()) {
			e.GuildUpdate(event);
		}
	}

}
