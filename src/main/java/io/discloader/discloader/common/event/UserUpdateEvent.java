/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.User;

/**
 * @author Perry Berman
 *
 */
public class UserUpdateEvent {
	
	/**
	 * The current user
	 */
	public User user;
	
	/**
	 * The user before the update
	 */
	public User oldUser;

	public UserUpdateEvent(User user, User oldUser) {
		this.user = user;
		this.oldUser = oldUser;
	}

}
