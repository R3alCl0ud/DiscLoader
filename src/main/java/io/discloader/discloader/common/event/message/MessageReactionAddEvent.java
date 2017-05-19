package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.user.IUser;

public class MessageReactionAddEvent extends DLEvent {

	private final IUser user;
	private final IMessage message;
	private final IReaction reaction;

	public MessageReactionAddEvent(IMessage message, IReaction reaction, IUser user) {
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
	 * @return the reaction
	 */
	public IReaction getReaction() {
		return reaction;
	}

	/**
	 * @return the user
	 */
	public IUser getUser() {
		return user;
	}

}
