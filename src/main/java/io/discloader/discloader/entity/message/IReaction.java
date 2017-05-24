package io.discloader.discloader.entity.message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.Permissions;

public interface IReaction {

	boolean didReact();

	int getCount();

	IEmoji getEmoji();

	IMessage getMessage();

	CompletableFuture<List<IUser>> getUsers();

	/**
	 * Deletes another user's reaction.<br>
	 * This method requires the {@link Permissions#MANAGE_MESSAGES } permission
	 * to be present on the current user.
	 * 
	 * @param user The user in which you are removing the reaction for.
	 * @return A Future that completes with no value if successful.
	 * @throws PermissionsException
	 */
	CompletableFuture<Void> removeUserReaction(IUser user);

	/**
	 * Deletes the reaction the current user has made for the message.
	 * 
	 * @return A Future that completes with no value if successful.
	 */
	CompletableFuture<Void> removeReaction();

}
