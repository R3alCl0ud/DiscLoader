/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.core.entity.user.User;

/**
 * @author Perry Berman
 *
 */
public class UserUpdateEvent extends DLEvent {

    /**
     * The current user
     */
    public User user;

    /**
     * The user before the update
     */
    public User oldUser;

    public UserUpdateEvent(User user, User oldUser) {
        super(user.loader);
        this.user = user;
        this.oldUser = oldUser;
    }

}
