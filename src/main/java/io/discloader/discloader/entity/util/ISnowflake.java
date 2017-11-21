package io.discloader.discloader.entity.util;

import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IMessageAttachment;
import io.discloader.discloader.entity.user.IUser;

/**
 * Discord utilizes Twitter's snowflake format for uniquely identifiable
 * descriptors (ID's). These ID's are up to 64bits in size (e.g. a uint64) and
 * therefore are always returned as strings in the API to prevent integer
 * overflows in some languages. Snowflake ID's are guaranteed to be unique
 * across all of Discord, except in some unique scenarios in which child objects
 * share their parents ID. <br>
 * <br>
 * All objects with IDs <u>must</u> implement this interface<br>
 * Objects that have Snowflake IDs.
 * <ul>
 * <li>{@link IChannel channels}</li>
 * <li>{@link IMessage messages}</li>
 * <li>{@link IGuildEmoji guild emojis}</li>
 * <li>{@link IGuild guilds}</li>
 * <li>{@link IUser users}</li>
 * <li>{@link IMessageAttachment message attachments}</li>
 * </ul>
 * 
 * @author Perry Berman
 */
@FunctionalInterface
public interface ISnowflake {

	/**
	 * @return The object's Snowflake ID.
	 */
	long getID();

	@Override
	int hashCode();

}
