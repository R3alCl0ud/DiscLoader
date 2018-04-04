package io.discloader.discloader.entity.presence;

public enum ActivityFlag {

	NONE(0), INSTANCE(1 << 0), JOIN(1 << 1), SPECTATE(1 << 2), JOIN_REQUEST(1 << 3), SYNC(1 << 4), PLAY(1 << 5);

	private final int flag;

	ActivityFlag(int f) {
		flag = f;
	}

	/**
	 * @return the flag
	 */
	public int toInt() {
		return flag;
	}

	public static ActivityFlag getFlagFromInteger(int i) {
		for (ActivityFlag flag : values()) {
			if ((flag.flag & i) != 0x0) {
				return flag;
			}
		}
		return NONE;
	}
}
