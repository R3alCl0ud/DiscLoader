package io.discloader.discloader.core.entity.emoji;

public enum EmojiCategory {
	PEOPLE("people"), NATURE("nature"), FOOD("food"), ACTIVITIES("activity"), TRAVEL("travel"), OBJECTS("objects"), SYMBOLS("symbols"), FLAGS("flags"), UNKNOWN("");

	public final String name;

	EmojiCategory(String name) {
		this.name = name;
	}

	public static EmojiCategory getByName(String name) {
		for (EmojiCategory category : EmojiCategory.values()) {
			if (category.name.equals(name)) return category;
		}
		return UNKNOWN;
	}
}
