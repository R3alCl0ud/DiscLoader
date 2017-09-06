package io.discloader.discloader.entity.message;

/**
 * @author Perry Berman
 */
public enum MessageType {
	MESSAGE(0), GROUP_ADD(1), GROUP_LEAVE(2), CALL(3), GROUP_NAME_CHANGE(4), GROUP_ICON_CHANGE(5), MESSAGE_PINNED(6), GUILD_MEMBER_JOIN(7);

	private int num;

	MessageType(int num) {
		this.num = num;
	}

	public static MessageType getMessageType(int type) {
		for (MessageType mType : values()) {
			if (mType.num == type) return mType;
		}
		return MESSAGE;
	}

}
