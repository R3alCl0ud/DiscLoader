package io.discloader.discloader.entity.channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;

public interface IGuildChannel extends IChannel {

	/**
	 * Creates a channel with the same information as this channel.
	 * 
	 * @return A Future that completes with the new channel, if successful.
	 */
	CompletableFuture<? extends IGuildChannel> clone();

	/**
	 * Deletes the channel from the guild
	 * 
	 * @return A Future that completes with the deleted channel if successful.
	 */
	CompletableFuture<? extends IGuildChannel> delete();

	/**
	 * Changes the channels settings
	 * 
	 * @param name The new name for the channel
	 * @param topic The new topic
	 * @param position The new position for the channel
	 * @param bitrate The new bitrate
	 * @param userLimit The new userLimit
	 * @return A Future that completes with a IGuildChannel if successful
	 */
	CompletableFuture<? extends IGuildChannel> edit(String name, String topic, int position, int bitrate, int userLimit);

	/**
	 * @return
	 */
	IGuild getGuild();

	/**
	 * Gets the members that can view/join this channel
	 * 
	 * @return A HashMap of GuildMember with access to view the channel
	 */
	HashMap<Long, IGuildMember> getMembers();

	String getName();

	int getPosition();

	IOverwrite overwriteFor(IRole role);

	/**
	 * Gets all of the channel's {@link IOverwrite overwrites} that applies to a
	 * {@link IGuildMember}
	 * 
	 * @param member The member of whom we are looking for overwrites that
	 *            apply.
	 * @author Perry Berman
	 * @return A {@link Collection} of {@link IOverwrite} objects.
	 * @version <strike>0.0.1</strike> Updated in version 0.1.1
	 * @since April 7, 2017.
	 */
	Collection<IOverwrite> overwritesOf(IGuildMember member);

	/**
	 * Evaluates the permissions for a member in the channel's {@link Guild}
	 * 
	 * @param iGuildMember The member whose permissions we are evaluating.
	 * @return A new Permissions object that contains {@literal this}, the
	 *         {@literal member}, and their evaluated permissions
	 *         {@link Integer}. <br>
	 *         null if the channel doesn't belong to a {@link Guild}
	 */
	IPermission permissionsFor(IGuildMember iGuildMember);

	/**
	 * Sets the name of the channel.
	 * 
	 * @param name New name for the channel who we are modifying.
	 * @return A completed future object with the new name set, if successful.
	 */
	CompletableFuture<? extends IGuildChannel> setName(String name);

	CompletableFuture<IOverwrite> setOverwrite(IOverwrite overwrite);

	/**
	 * Permission setting for a member in the channel's {@link Guild}
	 * 
	 * <pre>
	 * setPermissions(READ_MESSAGE &amp; SEND_MESSAGE, 0x00000000, role);
	 * </pre>
	 * 
	 * @param allow Raw integer representations for allowed permissions.
	 * @param deny Raw integer representations for denied permissions.
	 * @param member The {@link IGuildMember} to set an overwrite for.
	 * @return A Future that completes with the member's new permission
	 *         overwrite, if successful.
	 */
	CompletableFuture<IOverwrite> setPermissions(int allow, int deny, IGuildMember member);

	/**
	 * Permission setting for a member in the channel's {@link Guild}
	 * 
	 * <pre>
	 * setPermissions(READ_MESSAGE &amp; SEND_MESSAGE, 0x00000000, role);
	 * </pre>
	 * 
	 * @param allow Raw integer representations for allowed permissions.
	 * @param deny Raw integer representations for denied permissions.
	 * @param role The role to set an overwrite for.
	 * @return A Future that completes with the role's new permission overwrite
	 *         if successful.
	 */
	CompletableFuture<IOverwrite> setPermissions(int allow, int deny, Role role);

	/**
	 * Sets position in a channel's {@link Guild}
	 * 
	 * @param position The new possition of the channel
	 * @return A completed future with the position set.
	 */
	CompletableFuture<? extends IGuildChannel> setPosition(int position);
}
