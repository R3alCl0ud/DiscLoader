package io.discloader.discloader.entity;

/**
 * @author Perry Berman
 */
public enum Permissions {
	CREATE_INSTANT_INVITE(0x00000001, 1 << 0), KICK_MEMBERS(0x2, 1 << 1), BAN_MEMBERS(0x4, 1 << 2), ADMINISTRATOR(0x00000008, 1 << 3), MANAGE_CHANNELS(0x10, 1 << 4), MANAGE_GUILD(0x20, 1 << 5);

	private int value;
	private int flag;

	private Permissions(int value, int flag) {
		this.value = value;
		this.flag = flag;
	}

	public int getValue() {
		return value;
	}

	public int getFlag() {
		return flag;
	}
}
