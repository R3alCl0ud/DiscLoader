package io.discloader.discloader.entity.invite;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

/**
 * Represents an invite object in Discord's API
 * 
 * @author Perry Berman
 */
public interface IInvite {

	/**
	 * Attempts to delete the invite.
	 * 
	 * @return A {@link CompletableFuture} that completes with {@code this} invite
	 *         object if successful.
	 * @throws PermissionsException
	 */
	CompletableFuture<IInvite> delete();

	/**
	 * Returns the invite's unique identifier code.
	 * 
	 * @return The invite's unique identifier code.
	 */
	String getCode();

	/**
	 * Returns the partial {@link IInviteChanel channel} object of the
	 * {@link IChannel} that the invite is for.
	 * 
	 * @return The partial {@link IInviteChanel channel} object of the
	 *         {@link IChannel} that the invite is for.
	 */
	IInviteChannel getChannel();

	/**
	 * Returns the partial {@link IInviteGuild guild} object of the {@link IGuild }
	 * that the invite is for.
	 * 
	 * @return The partial {@link IInviteGuild guild} object of the {@link IGuild}
	 *         that the invite is for.
	 */
	IInviteGuild getGuild();

	/**
	 * Returns the {@link IUser} who created {@code this} invite.
	 * 
	 * @return The {@link IUser} who created {@code this} invite.
	 */
	IUser getInviter();

	int getMaxAge();

	int getMaxUses();

	int getUses();

	boolean isTemporary();

	boolean isValid();

	/**
	 * Returns an {@link OffsetDateTime} object of the time at which {@code this}
	 * invite was created.
	 * 
	 * @return An {@link OffsetDateTime} object of the time at which {@code this}
	 *         invite was created.
	 */
	OffsetDateTime getCreatedAt();

}
