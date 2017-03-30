package io.discloader.discloader.common.event.guild.emoji;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;

public class GuildEmojiCreateEvent extends DLEvent {

	private IGuild guild;

	private IGuildEmoji emoji;

	public GuildEmojiCreateEvent(IGuildEmoji emoji) {
		super(emoji.getLoader());
		this.emoji = emoji;
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

}
