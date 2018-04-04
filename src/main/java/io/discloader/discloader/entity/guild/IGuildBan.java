package io.discloader.discloader.entity.guild;

import io.discloader.discloader.entity.user.IUser;

public interface IGuildBan {

	/**
	 * Returns the reason for the ban.
	 * 
	 * @return The reason for the ban.
	 */
	String getReason();

	/**
	 * Returns an {@link IUser} object representing the banned user.
	 * 
	 * @return An {@link IUser} object representing the banned user.
	 */
	IUser getUser();

}
