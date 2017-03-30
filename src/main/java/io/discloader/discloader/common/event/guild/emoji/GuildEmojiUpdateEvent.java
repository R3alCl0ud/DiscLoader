package io.discloader.discloader.common.event.guild.emoji;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;

public class GuildEmojiUpdateEvent extends DLEvent {

	private IGuild guild;

	private final IGuildEmoji emoji;
	private final IGuildEmoji oldEmoji;

	public GuildEmojiUpdateEvent(IGuildEmoji emoji, IGuildEmoji oldEmoji) {
		super(emoji.getLoader());
		this.emoji = emoji;
		this.oldEmoji = oldEmoji;
		guild = emoji.getGuild();
	}

	/**
	 * @return the emoji
	 */
	public IGuildEmoji getEmoji() {
		return emoji;
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return guild;
	}

	/**
	 * @return the oldEmoji
	 */
	public IGuildEmoji getOldEmoji() {
		return oldEmoji;
	}

}
