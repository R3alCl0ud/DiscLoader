package io.discloader.discloader.entity.message;

import java.util.List;
import java.util.regex.Pattern;

import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IMentions {
	final String channelRegex = "(<#(\\d+)>)";
	final String userRegex = "(<!?@(\\d+)>)";
	final String roleRegex = "(<@&(\\d+)>)";
	final Pattern channelPattern = Pattern.compile(channelRegex, Pattern.MULTILINE);
	final Pattern userPattern = Pattern.compile(userRegex, Pattern.MULTILINE);
	final Pattern rolePattern = Pattern.compile(roleRegex, Pattern.MULTILINE);

	IMessage getMessage();

	List<IRole> getRoles();

	List<IUser> getUsers();

	/**
	 * Whether or not you were mentioned
	 * 
	 * @return true if you were mentioned
	 */
	boolean isMentioned();

	boolean isMentioned(IChannel channel);

	/**
	 * @param member
	 * @return {@code true} if the member was mentioned, {@code false} otherwise.
	 */
	boolean isMentioned(IGuildMember member);

	boolean isMentioned(IMentionable mentionable);

	/**
	 * @param user
	 * @return {@code true} if the user was mentioned, {@code false} otherwise.
	 */
	boolean isMentioned(IUser user);

	/**
	 * Whether or not {@literal @everyone} was mentioned
	 * 
	 * @return true if the {@link #getMessage message}'s content contains
	 *         {@literal @everyone}, false otherwise
	 */
	boolean mentionedEveryone();

	/**
	 * Calculates the number of {@link IRole roles} and {@link IUser users}
	 * mentioned and returns that number.
	 * 
	 * @return the number of roles and users mentioned
	 */
	int size();

	List<IMentionable> toList();

}
