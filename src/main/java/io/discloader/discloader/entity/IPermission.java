package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.util.Permissions;

public interface IPermission {

	long toLong();

	IGuildChannel getChannel();

	/**
	 * Checks if the member has the specified permission. <br>
	 * {@code boolean sendMessages =
	 * channel.permissionsFor(member).hasPermission({@link
	 * Permissions#SEND_MESSAGES});}
	 * 
	 * @param permission
	 *            A {@link Permissions} constant
	 * @param explicit
	 *            Whether or not the member explicitly has the permission
	 * @return {@code true} if the user has the specified {@link Permissions
	 *         permission} or the {@link Permissions#ADMINISTRATOR} permission.<br>
	 *         {@code false} otherwise.
	 */
	boolean hasPermission(Permissions permission, boolean explicit);

	/**
	 * Checks if the member has the specified permission(s). <br>
	 * {@code boolean sendMessages =
	 * channel.permissionsFor(member).hasPermission({@link
	 * Permissions#SEND_MESSAGES});}
	 * 
	 * @param permissions
	 *            One or more {@link Permissions} constant so check for.
	 * @return {@code true} if the user has the specified {@link Permissions
	 *         permissions}. {@code false} otherwise.
	 */
	boolean hasPermission(Permissions... permissions);

	/**
	 * @param permissions
	 * @return {@code true} if the {@link IGuildMember member} has any of the
	 *         specified {@link Permissions permissions}. {@code false} otherwise.
	 */
	boolean hasAny(Permissions... permissions);

}
