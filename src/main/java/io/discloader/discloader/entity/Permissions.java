package io.discloader.discloader.entity;

/**
 * Enum containing permissions constants
 * 
 * @author Perry Berman
 */
public enum Permissions {
	/**
	 * Allows creation of instant invites
	 */
	CREATE_INSTANT_INVITE(0x1, 1 << 0),
	/**
	 * Allows kicking members
	 */
	KICK_MEMBERS(0x2, 1 << 1),
	/**
	 * Allows banning members
	 */
	BAN_MEMBERS(0x4, 1 << 2),
	/**
	 * Allows all permissions and bypasses channel permission overwrites
	 */
	ADMINISTRATOR(0x8, 1 << 3),
	/**
	 * Allows management and editing of channels
	 */
	MANAGE_CHANNELS(0x10, 1 << 4),
	/**
	 * Allows management and editing of the guild
	 */
	MANAGE_GUILD(0x20, 1 << 5),
	/**
	 * Allows for the addition of reactions to messages
	 */
	ADD_REACTIONS(0x40, 1 << 6),
	/**
	 * Allows reading messages in a channel. The channel will not appear for
	 * users without this permission
	 */
	READ_MESSAGES(0x400, 1 << 10),
	/**
	 * Allows for sending messages in a channel.
	 */
	SEND_MESSAGES(0x800, 1 << 11),
	/**
	 * Allows for sending of /tts messages
	 */
	SEND_TTS_MESSAGES(0x1000, 1 << 12),
	/**
	 * Allows for deletion of other users messages
	 */
	MANAGE_MESSAGES(0x2000, 1 << 13),
	/**
	 * Links sent by this user will be auto-embedded
	 */
	EMBED_LINKS(0x4000, 1 << 14),
	/**
	 * Allows for uploading images and files
	 */
	ATTACH_FILES(0x8000, 1 << 15),
	/**
	 * Allows for reading of message history
	 */
	READ_MESSAGE_HISTORY(0x10000, 1 << 16),
	/**
	 * Allows for using the @everyone tag to notify all users in a channel, and
	 * the @here tag to notify all online users in a channel
	 */
	MENTION_EVERYONE(0x20000, 1 << 17),
	/**
	 * Allows the usage of custom emojis from other servers
	 */
	USE_EXTERNAL_EMOJIS(0x40000, 1 << 18),
	/**
	 * Allows for joining of a voice channel
	 */
	CONNECT(0x100000, 1 << 20),
	/**
	 * Allows for speaking in a voice channel
	 */
	SPEAK(0x200000, 1 << 21),
	/**
	 * Allows for muting members in a voice channel
	 */
	MUTE_MEMBERS(0x400000, 1 << 22),
	/**
	 * Allows for deafening of members in a voice channel
	 */
	DEAFEN_MEMBERS(0x800000, 1 << 23),
	/**
	 * Allows for moving of members between voice channels
	 */
	MOVE_MEMBERS(0x1000000, 1 << 24),
	/**
	 * Allows for using voice-activity-detection in a voice channel
	 */
	USE_VAD(0x2000000, 1 << 25),
	/**
	 * Allows for modification of own nickname
	 */
	CHANGE_NICKNAME(0x4000000, 1 << 26),
	/**
	 * Allows for modification of other users nicknames
	 */
	MANAGE_NICKNAMES(0x8000000, 1 << 27),
	/**
	 * Allows management and editing of roles
	 */
	MANAGE_ROLES(0x10000000, 1 << 28),
	/**
	 * Allows management and editing of webhooks
	 */
	MANAGE_WEBHOOKS(0x20000000, 1 << 29),
	/**
	 * Allows management and editing of emojis
	 */
	MANAGE_EMOJIS(0x40000000, 1 << 30);

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
