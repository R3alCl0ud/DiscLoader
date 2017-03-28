package io.discloader.discloader.entity;

/**
 * @author Perry Berman
 */
public enum Permissions {
	CREATE_INSTANT_INVITE(0x1, 1 << 0), KICK_MEMBERS(0x2, 1 << 1), BAN_MEMBERS(0x4, 1 << 2), ADMINISTRATOR(0x8, 1 << 3), MANAGE_CHANNELS(0x10, 1 << 4), MANAGE_GUILD(0x20, 1 << 5), ADD_REACTIONS(0x40, 1 << 6), READ_MESSAGES(0x400, 1 << 10),
	SEND_MESSAGES(0x800, 1 << 11), SEND_TTS_MESSAGES(0x1000, 1 << 12), MANAGE_MESSAGES(0x2000, 1 << 13), EMBED_LINKS(0x4000, 1 << 14), ATTACH_FILES(0x8000, 1 << 15), READ_MESSAGE_HISTORY(0x10000, 1 << 16),
	MENTION_EVERYONE(0x20000, 1 << 17), USE_EXTERNAL_EMOJIS(0x40000, 1 << 18), CONNECT(0x100000, 1 << 20), SPEAK(0x200000, 1 << 21), MUTE_MEMBERS(0x400000, 1 << 22), DEAFEN_MEMBERS(0x800000, 1 << 23), MOVE_MEMBERS(0x1000000, 1 << 24),
	USE_VAD(0x2000000, 1 << 25), CHANGE_NICKNAME(0x4000000, 1 << 26), MANAGE_NICKNAMES(0x8000000, 1 << 27), MANAGE_ROLES(0x10000000, 1 << 28), MANAGE_WEBHOOKS(0x20000000, 1 << 29), MANAGE_EMOJIS(0x40000000, 1 << 30);
	
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
