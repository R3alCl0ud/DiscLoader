package io.discloader.discloader.entity.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.TimeZone;

public class SnowflakeUtil {

	public static final long DISCORD_EPOCH = 1420070400000L;

	public static OffsetDateTime creationTime(ISnowflake snowflake) {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli((snowflake.getID() >> 22) + DISCORD_EPOCH), TimeZone.getDefault().toZoneId());
	}

	public static OffsetDateTime getTime(String s) {
		return OffsetDateTime.parse(s);
	}

	public static long parse(String id) {
		return Long.parseUnsignedLong(id, 10);
	}

	/**
	 * @deprecated Use {@link #toString(ISnowflake)} instead
	 */
	public static String asString(ISnowflake snowflake) {
		return toString(snowflake);
	}

	public static String toString(ISnowflake snowflake) {
		if (snowflake == null) return null;
		if (snowflake.getID() == 0l) return snowflake.toString();
		return Long.toUnsignedString(snowflake.getID(), 10);
	}
}
