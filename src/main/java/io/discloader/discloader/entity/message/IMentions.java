package io.discloader.discloader.entity.message;

import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IMentions {

	IMessage getMessage();

	/**
	 * Whether or not you were mentioned
	 * 
	 * @return true if you were mentioned
	 */
	boolean isMentioned();

	/**
	 * @param member
	 * @return {@code true} if the member was mentioned, {@code false}
	 *         otherwise.
	 */
	boolean isMentioned(IGuildMember member);

	/**
	 * @param user
	 * @return {@code true} if the user was mentioned, {@code false} otherwise.
	 */
	boolean isMentioned(IUser user);

	/**
	 * Whether or not {@literal @everyone} was mentioned
	 * 
	 * @return true if the {@link #message}'s content contains
	 *         {@literal @everyone}, false otherwise
	 */
	boolean mentionedEveryone();

}
