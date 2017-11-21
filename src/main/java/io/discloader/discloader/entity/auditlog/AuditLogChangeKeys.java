package io.discloader.discloader.entity.auditlog;

public enum AuditLogChangeKeys {

	NAME("string", "guild"), ICON_HASH("string", "guild"), SPLASH_HASH("string", "guild"), OWNER_ID("snowflake", "guild"), REGION("string", "guild"), AFK_CHANNEL_ID("snowflake", "guild"), AFK_TIMEOUT("int", "guild"),
	MFA_LEVEL("int", "guild"), VERIFICATION_LEVEL("int", "guild"), EXPLICIT_CONTENT_FILTER("int", "guild"), DEFAULT_MESSAGE_NOTIFICATIONS("int", "guild"), VANITY_URL_CODE("string", "guild"), $ADD("role[]", "guild"),
	$REMOVE("role[]", "guild"), PRUNE_DELETE_DAYS("int", "guild"), WIDGET_ENABLED("bool", "guild"), WIDGET_CHANNEL_ID("snowflake", "guild"), POSITION("int", "channel"), TOPIC("string", "channel"), BITRATE("int", "channel"),
	PERMISSINO_OVERWRITES("overwrite[]", "channel"), NSFW("bool", "channel"), APPLICATION_ID("snowflake", "channel"), PERMISSIONS("int", "role"), COLOR("int", "role"), ALLOW("int", "role"), DENY("int", "role"), HOIST("bool", "role"),
	MENTIONABLE("bool", "role"), CODE("string", "invite"), CHANNEL_ID("snowflake", "invite"), INVITER_ID("snowflake", "invite"), MAX_USES("int", "invite"), USES("int", "invite"), MAX_AGE("int", "invite"), TEMPORARY("bool", "invite"),
	DEAF("bool", "user"), MUTE("bool", "user"), NICK("string", "user"), AVATAR_HASH("string", "user"), ID("snowflake", "any"), TYPE("string|int", "any"), ANY("any", "any");

	private final String type, objChanged;

	AuditLogChangeKeys(String type, String objC) {
		this.type = type;
		this.objChanged = objC;
	}

	public static AuditLogChangeKeys getKey(String name) {
		for (AuditLogChangeKeys key : values()) {
			if (key.toString().equalsIgnoreCase(name)) return key;
		}
		return ANY;
	}

	/**
	 * @return {@code objChanged}
	 */
	public String getObjChanged() {
		return objChanged;
	}

	/**
	 * @return {@code type}
	 */
	public String getType() {
		return type;
	}

}
