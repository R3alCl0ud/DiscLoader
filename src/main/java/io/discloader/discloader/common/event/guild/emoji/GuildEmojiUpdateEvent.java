package io.discloader.discloader.common.event.guild.emoji;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Emoji;
import io.discloader.discloader.entity.guild.Guild;

public class GuildEmojiUpdateEvent extends DLEvent {

	private Guild guild;

	private final Emoji emoji;
	private final Emoji oldEmoji;

	public GuildEmojiUpdateEvent(Emoji emoji, Emoji oldEmoji) {
		super(emoji.loader);
		this.emoji = emoji;
		this.oldEmoji = oldEmoji;
		guild = emoji.guild;
	}

	/**
	 * @return the emoji
	 */
	public Emoji getEmoji() {
		return emoji;
	}

	/**
	 * @return the guild
	 */
	public Guild getGuild() {
		return guild;
	}

	/**
	 * @return the oldEmoji
	 */
	public Emoji getOldEmoji() {
		return oldEmoji;
	}

}
