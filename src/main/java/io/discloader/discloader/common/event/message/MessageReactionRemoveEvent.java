package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.user.IUser;

public class MessageReactionRemoveEvent extends DLEvent {

	private final IUser user;
	private final IMessage message;
	private final IReaction reaction;

	public MessageReactionRemoveEvent(IMessage message, IReaction reaction, IUser user) {
		super(message.getLoader());
		this.message = message;
		this.reaction = reaction;
		this.user = user;
	}

	/**
	 * @return the message
	 */
	public IMessage getMessage() {
		return message;
	}

	/**
	 * @return the reaction that was removed
	 */
	public IReaction getReaction() {
		return reaction;
	}

	/**
	 * @return the user that removed the reaction
	 */
	public IUser getUser() {
		return user;
	}

}
