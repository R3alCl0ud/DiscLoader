package io.discloader.discloader.entity.channel;

import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IPrivateChannel extends ITextChannel {

	IUser getRecipient();
}
