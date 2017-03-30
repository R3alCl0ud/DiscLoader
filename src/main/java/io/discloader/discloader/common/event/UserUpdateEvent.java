/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 *
 */
public class UserUpdateEvent extends DLEvent {
	
	/**
	 * The current user
	 */
	public IUser user;
	
	/**
	 * The user before the update
	 */
	public IUser oldUser;
	
	public UserUpdateEvent(IUser user, IUser oldUser) {
		super(user.getLoader());
		this.user = user;
		this.oldUser = oldUser;
	}
	
}
