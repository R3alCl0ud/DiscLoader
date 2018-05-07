package io.discloader.discloader.entity.message;

import java.util.List;
import java.util.regex.Pattern;

import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IMentions {
	/**
	 * The first group would be the entire raw mention. The second group is the
	 * channel's id.
	 */
	final String channelRegex = "(<#(\\d+)>)";
	/**
	 * The first group would be the entire raw mention. The second group is the
	 * user's id.
	 */
	final String userRegex = "(<!?@(\\d+)>)";
	/**
	 * The first group would be the entire raw mention. The second group is the
	 * role's id.
	 */
	final String roleRegex = "(<@&(\\d+)>)";
	/**
	 * The first group would be the entire raw mention. The second group is the
	 * channel's id.
	 */
	final Pattern channelPattern = Pattern.compile(channelRegex, Pattern.MULTILINE);
	/**
	 * The first group would be the entire raw mention. The second group is the
	 * user's id.
	 */
	final Pattern userPattern = Pattern.compile(userRegex, Pattern.MULTILINE);
	/**
	 * The first group would be the entire raw mention. The second group is the
	 * role's id.
	 */
	final Pattern rolePattern = Pattern.compile(roleRegex, Pattern.MULTILINE);

	IMessage getMessage();

	List<IRole> getRoles();

	List<IUser> getUsers();

	List<IGuildChannel> getChannels();

	List<IGuildMember> getMembers();

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
