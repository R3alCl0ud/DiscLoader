package io.discloader.discloader.entity.channel;

public enum ChannelTypes {
	TEXT(0), DM(1), VOICE(2), GROUP(3), CATEGORY(4), CHANNEL(5);

	private int identifier;

	ChannelTypes(int identifier) {
		this.identifier = identifier;
	}

	public int toInt() {
		return identifier;
	}

	public static ChannelTypes fromCode(int code) {
		for (ChannelTypes type : values()) {
			if (type.identifier == code) return type; // if the identifier
														// number is the same as
														// code return the
														// channel type
		}
		return CHANNEL; // just return text by default
	}
}
