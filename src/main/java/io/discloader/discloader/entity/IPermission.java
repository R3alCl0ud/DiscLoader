package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.util.Permissions;

public interface IPermission {

	int asInt();

	IGuildChannel getChannel();

	/**
	 * Checks if the member has the specified permission. <br>
	 * {@code boolean sendMessages =
	 * channel.permissionsFor(member).hasPermission({@link Permissions#SEND_MESSAGES});}
	 * 
	 * @param permission A {@link Permissions} constant
	 * @return {@code true} if the user has the specified {@link Permissions
	 *         permission}. {@code false} otherwise.
	 */
	boolean hasPermission(Permissions permission);

	/**
	 * Checks if the member has the specified permission. <br>
	 * {@code boolean sendMessages =
	 * channel.permissionsFor(member).hasPermission({@link Permissions#SEND_MESSAGES});}
	 * 
	 * @param permission A {@link Permissions} constant
	 * @param explicit Whether or not the member explicitly has the permission
	 * @return {@code true} if the user has the specified {@link Permissions
	 *         permission} or the
	 *         {@link Permissions#ADMINISTRATOR} permission.<br>
	 *         {@code false} otherwise.
	 */
	boolean hasPermission(Permissions permission, boolean explicit);

}
