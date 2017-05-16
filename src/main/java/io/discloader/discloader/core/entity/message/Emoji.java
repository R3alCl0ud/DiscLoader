package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.network.json.EmojiJSON;

public class Emoji implements IEmoji {

	private DiscLoader loader;
	private String name;

	public Emoji(EmojiJSON data, IReaction reaction) {
		loader = reaction.getMessage().getLoader();
		name = data.name;
	}

	@Override
	public long getID() {
		return 0l;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	@Override
	public String toString() {
		return name;
	}

}
