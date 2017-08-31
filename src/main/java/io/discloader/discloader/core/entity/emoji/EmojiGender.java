package io.discloader.discloader.core.entity.emoji;

public enum EmojiGender {

	MALE("2642", "MALE"), FEMALE("2640", "FEMALE"), UNKNOWN("0", "UNKNOWN");

	private final String code, name;

	EmojiGender(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return String.valueOf(Character.toChars(Integer.parseInt(code, 16)));
	}

	public static EmojiGender getByName(String name) {
		for (EmojiGender gender : EmojiGender.values())
			if (gender.name.equalsIgnoreCase(name)) return gender;
		return UNKNOWN;
	}

	public static EmojiGender getByCode(String code) {
		if (code != null) for (EmojiGender gender : EmojiGender.values())
			if (gender.code.equalsIgnoreCase(code) || code.contains(gender.code)) return gender;
		return UNKNOWN;
	}
}
