package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.core.entity.guild.Emoji;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.ReactionJSON;

public class Reaction {

	private boolean me;
	private int count;
	private Emoji emoji;
	private IMessage message;

	public Reaction(ReactionJSON data, IMessage message) {
		this.message = message;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the emoji
	 */
	public Emoji getEmoji() {
		return emoji;
	}

	public IMessage getMessage() {
		return message;
	}

	/**
	 * @return the me
	 */
	public boolean isMe() {
		return me;
	}
}
