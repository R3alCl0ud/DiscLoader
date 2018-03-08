package io.discloader.discloader.entity.message;

/**
 * Enumeration containing constants for different Rich Presence Message Activity Types
 * 
 * @author Perry Berman
 */
public enum MessageActivityType {
	UNKNOWN(0), JOIN(1), SPECTATE(2), LISTEN(3), JOIN_REQUEST(5);

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
