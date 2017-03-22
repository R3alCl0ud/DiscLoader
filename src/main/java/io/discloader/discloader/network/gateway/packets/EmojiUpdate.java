package io.discloader.discloader.network.gateway.packets;

import java.util.HashMap;

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

		// guild.emojis;
		HashMap<String, Emoji> emojis = new HashMap<>();
		for (EmojiJSON e : data.emojis) {
			Emoji emoji = new Emoji(e, guild);
			emojis.put(e.id, emoji);
			if (!guild.emojis.containsKey(e.id)) {
				guild.emojis.put(e.id, emoji);
				
			}
		}
		if (shouldEmit()) {
			loader.emit(Events.GUILD_EMOJIS_UPDATE, guild.emojis);
			for (IEventListener e : loader.handlers) {
				e.GuildEmojisUpdate(guild.emojis);
			}
		}

	}

}
