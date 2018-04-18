package io.discloader.discloader.entity.channel;

public enum ChannelTypes {
	/**
	 * Text channels within a guild.
	 */
	TEXT(0),
	/**
	 * Direct Messaging channels.
	 */
	DM(1),
	/**
	 * Voice channels within a guild.
	 */
	VOICE(2),
	/**
	 * Group DM channels.
	 */
	GROUP(3),
	/**
	 * Category channels within a guild.
	 */
	CATEGORY(4),
	/**
	 * All other channels.
	 */
	CHANNEL(5);

	private int identifier;

	ChannelTypes(int identifier) {
		this.identifier = identifier;
	}

	public int toInt() {
		return identifier;
	}

	public static ChannelTypes fromCode(int code) {
		for (ChannelTypes type : values()) {
			if (type.identifier == code)
				return type; // if the identifier number is the same as code return the channel type
		}
		return CHANNEL; // just return text by default
	}
}
