package io.discloader.discloader.network.gateway.packets;

import java.util.HashMap;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
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

		HashMap<String, Emoji> emojis = new HashMap<>();
		for (EmojiJSON e : data.emojis) {
			Emoji emoji = new Emoji(e, guild);
			emojis.put(e.id, emoji);
		}

		if (shouldEmit()) {
			if (data.emojis.length == guild.emojis.size()) {
				Emoji oldEmoji;
				for (Emoji emoji : emojis.values()) {
					oldEmoji = guild.emojis.get(emoji.id);
					if (emoji.id.equals(oldEmoji.id) && !emoji.equals(oldEmoji)) {
						GuildEmojiUpdateEvent event = new GuildEmojiUpdateEvent(emoji, oldEmoji);
						loader.emit(Events.GUILD_EMOJI_UPDATE,  event);
						loader.emit(event);
					}
				}
			} else if (data.emojis.length < guild.emojis.size()) {
				for (Emoji emoji : guild.emojis.values()){
					if (!emojis.containsKey(emoji.id)) {
						GuildEmojiDeleteEvent event = new GuildEmojiDeleteEvent(emoji);
						loader.emit("GuildEmojiDelete",  event);
						loader.emit(event);
					}
				}
			} else if (data.emojis.length > guild.emojis.size()) {
				for (Emoji emoji : emojis.values()){
					if (!guild.emojis.containsKey(emoji.id)) {
						GuildEmojiCreateEvent event = new GuildEmojiCreateEvent(emoji);
						loader.emit("GuildEmojiCreate",  event);
						loader.emit(event);
					}
				}
			}

			loader.emit(Events.GUILD_EMOJIS_UPDATE, guild.emojis);
			for (IEventListener e : loader.handlers) {
				e.GuildEmojisUpdate(guild.emojis);
			}
		}

	}

}
