package io.discloader.discloader.entity.channel;

import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IPrivateChannel extends IChannel, ITextChannel<IPrivateChannel> {

	IUser getRecipient();
}
