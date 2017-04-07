package io.discloader.discloader.network.gateway.packets;

import java.util.HashMap;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.core.entity.guild.Emoji;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.EmojiJSON;
import io.discloader.discloader.network.json.EmojiUpdateJSON;
import io.discloader.discloader.util.DLUtil.Events;

public class EmojiUpdate extends AbstractHandler {

	public EmojiUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet.d);
		EmojiUpdateJSON data = gson.fromJson(d, EmojiUpdateJSON.class);
		IGuild guild = loader.guilds.get(data.guild_id);

		HashMap<String, IGuildEmoji> emojis = new HashMap<>();
		for (EmojiJSON e : data.emojis) {
			IGuildEmoji emoji = new Emoji(e, guild);
			emojis.put(e.id, emoji);
		}

		if (shouldEmit()) {
			if (data.emojis.length == guild.getEmojis().size()) {
				IGuildEmoji oldEmoji;
				for (IGuildEmoji emoji : emojis.values()) {
					oldEmoji = guild.getEmojis().get(emoji.getID());
					if (emoji.getID() == oldEmoji.getID() && !emoji.equals(oldEmoji)) {
						GuildEmojiUpdateEvent event = new GuildEmojiUpdateEvent(emoji, oldEmoji);
						loader.emit(Events.GUILD_EMOJI_UPDATE, event);
						loader.emit(event);
					}
				}
			} else if (data.emojis.length < guild.getEmojis().size()) {
				for (IGuildEmoji emoji : guild.getEmojis().values()) {
					if (!emojis.containsKey(emoji.getID())) {
						GuildEmojiDeleteEvent event = new GuildEmojiDeleteEvent(emoji);
						loader.emit("GuildEmojiDelete", event);
						loader.emit(event);
					}
				}
			} else if (data.emojis.length > guild.getEmojis().size()) {
				for (IGuildEmoji emoji : emojis.values()) {
					if (!guild.getEmojis().containsKey(emoji.getID())) {
						GuildEmojiCreateEvent event = new GuildEmojiCreateEvent(emoji);
						loader.emit("GuildEmojiCreate", event);
						loader.emit(event);
					}
				}
			}

			loader.emit(Events.GUILD_EMOJIS_UPDATE, guild.getEmojis());
			for (IEventListener e : loader.handlers) {
				e.GuildEmojisUpdate(guild.getEmojis());
			}
		}

	}

}
