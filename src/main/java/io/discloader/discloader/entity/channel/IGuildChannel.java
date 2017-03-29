package io.discloader.discloader.entity.channel;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.entity.Overwrite;
import io.discloader.discloader.common.entity.Permission;
import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.common.entity.guild.GuildMember;
import io.discloader.discloader.common.entity.guild.Role;

public interface IGuildChannel extends IChannel {

	/**
	 * Gets the members that can view/join this channel
	 * 
	 * @return A HashMap of GuildMember with access to view the channel
	 */
	HashMap<String, GuildMember> getMembers();

	/**
	 * Gets all of the channel's {@link Overwrite overwrites} that applies to a
	 * {@link GuildMember}
	 * 
	 * @param member The member of whom we are looking for overwrites that
	 *            apply.
	 * @author Perry Berman
	 * @return A {@link HashMap} of overwrite objects, indexed by
	 *         {@link Overwrite#id}
	 * @since 0.0.1
	 */
	HashMap<String, Overwrite> overwritesOf(GuildMember member);

	Overwrite overwriteFor(Role role);

	/**
	 * Evaluates the permissions for a member in the channel's {@link Guild}
	 * 
	 * @param member The member whose permissions we are evaluating.
	 * @return A new Permissions object that contains {@literal this}, the
	 *         {@literal member}, and their evaluated permissions
	 *         {@link Integer}. <br>
	 *         null if the channel doesn't belong to a {@link Guild}
	 */
	Permission permissionsFor(GuildMember member);

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
	 * Deletes the channel from the guild
	 * 
	 * @return A Future that completes with the deleted channel if successful.
	 */
	CompletableFuture<? extends IGuildChannel> delete();

	/**
	 * Creates a channel with the same information as this channel.
	 * 
	 * @return A Future that completes with the new channel, if successful.
	 */
	CompletableFuture<? extends IGuildChannel> clone();

	/**
	 * Sets the name of the channel.
	 * 
	 * @param name New name for the channel who we are modifying.
	 * @return A completed future object with the new name set, if successful.
	 */
	CompletableFuture<? extends IGuildChannel> setName(String name);

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
	CompletableFuture<Overwrite> setPermissions(int allow, int deny, Role role);

	/**
	 * Permission setting for a member in the channel's {@link Guild}
	 * 
	 * <pre>
	 * setPermissions(READ_MESSAGE &amp; SEND_MESSAGE, 0x00000000, role);
	 * </pre>
	 * 
	 * @param allow Raw integer representations for allowed permissions.
	 * @param deny Raw integer representations for denied permissions.
	 * @param member The member to set an overwrite for.
	 * @return A Future that completes with the member's new permission
	 *         overwrite,
	 *         if successful.
	 */
	CompletableFuture<Overwrite> setPermissions(int allow, int deny, GuildMember member);

	/**
	 * Sets position in a channel's {@link Guild}
	 * 
	 * @param position The new possition of the channel
	 * @return A completed future with the position set.
	 */
	CompletableFuture<? extends IGuildChannel> setPosition(int position);

}
