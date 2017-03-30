package io.discloader.discloader.common.event.guild.emoji;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;

public class GuildEmojiDeleteEvent extends DLEvent {

	private IGuild guild;

	private IGuildEmoji emoji;

	public GuildEmojiDeleteEvent(IGuildEmoji emoji2) {
		super(emoji2.getLoader());
		emoji = emoji2;
		guild = emoji2.getGuild();
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
}
