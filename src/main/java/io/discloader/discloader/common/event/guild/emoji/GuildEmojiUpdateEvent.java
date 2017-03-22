package io.discloader.discloader.common.event.guild.emoji;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Emoji;
import io.discloader.discloader.entity.guild.Guild;

public class GuildEmojiUpdateEvent extends DLEvent {

	private Guild guild;

	private Emoji emoji;

	public GuildEmojiUpdateEvent(Emoji emoji) {
		super(emoji.loader);
		setEmoji(emoji);
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
	 * @param emoji the emoji to set
	 */
	protected void setEmoji(Emoji emoji) {
		this.emoji = emoji;
	}

	/**
	 * @param guild the guild to set
	 */
	protected void setGuild(Guild guild) {
		this.guild = guild;
	}

}
