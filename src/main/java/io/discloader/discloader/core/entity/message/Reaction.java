package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.core.entity.guild.GuildEmoji;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.network.json.ReactionJSON;

public class Reaction implements IReaction {

	private boolean me;
	private int count;
	private IEmoji emoji;
	private IMessage message;

	public Reaction(ReactionJSON data, IMessage message) {
		this.message = message;
		me = data.me;
		count = data.count;
		if (data.emoji.id != null) emoji = new GuildEmoji(data.emoji, message.getGuild());
		else emoji = new Emoji(data.emoji, this);
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
	public IEmoji getEmoji() {
		return emoji;
	}

	public IMessage getMessage() {
		return message;
	}

	@Override
	public boolean didReact() {
		return me;
	}
}
