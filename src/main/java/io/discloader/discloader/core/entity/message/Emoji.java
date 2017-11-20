package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.network.json.EmojiJSON;

public class Emoji implements IEmoji {

	private DiscLoader loader;
	private String name;

	public Emoji(EmojiJSON data, DiscLoader loader) {
		this.loader = loader;
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

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Emoji))
			return false;
		Emoji emoji = (Emoji) object;

		return (this == emoji) || getName().equals(emoji.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

}
