package io.discloader.discloader.entity.message;

public enum MessageActivityType {
	JOIN(1), SPECTATE(2), LISTEN(3), JOIN_REQUEST(5), UNKNOWN(0);

	private int value;

	MessageActivityType(int value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	public static MessageActivityType getTypeFromInt(int value) {
		for (MessageActivityType mat : values()) {
			if (mat.getValue() == value)
				return mat;
		}
		return UNKNOWN;
	}
}
