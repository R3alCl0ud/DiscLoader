package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.guild.Emoji;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.EmojiJSON;
import io.discloader.discloader.network.json.EmojiUpdateJSON;
import io.discloader.discloader.util.DLUtil.Events;

public class EmojiUpdate extends DLPacket {

	public EmojiUpdate(DiscSocket socket) {
		super(socket);
	}

	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		EmojiUpdateJSON data = gson.fromJson(d, EmojiUpdateJSON.class);
		Guild guild = loader.guilds.get(data.guild_id);

		guild.emojis.clear();
		for (EmojiJSON e : data.emojis) {
			guild.emojis.put(e.id, new Emoji(e, guild));
		}
		if (shouldEmit()) {
			loader.emit(Events.GUILD_EMOJIS_UPDATE, guild.emojis);
			for (IEventListener e : DiscLoader.handlers.values()) {
				e.GuildEmojisUpdate(guild.emojis);
			}
		}

	}

}
