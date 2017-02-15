/**
 * 
 */
package io.discloader.discloader.events;

import io.discloader.discloader.objects.structures.User;

/**
 * @author Perry Berman
 *
 */
public class UserUpdateEvent {
	public User user;
	public User oldUser;

	/**
	 * 
	 */
	public UserUpdateEvent(User user, User oldUser) {
		this.user = user;
		this.oldUser = oldUser;
	}

}
