package io.discloader.discloader.entity.emoji;

import java.util.HashMap;
import java.util.Map;

public class EmojiCategory {

	private final Map<String, DiscordEmoji> emojis;
	private final String name;

	public EmojiCategory(String name, DiscordEmoji... emojis) {
		this.emojis = new HashMap<>();
		this.name = name;
		for (DiscordEmoji emoji : emojis) {
			this.emojis.put(emoji.getName(), emoji);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the emojis
	 */
	public Map<String, DiscordEmoji> getEmojis() {
		return emojis;
	}
}
