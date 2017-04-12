package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.message.Reaction;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;

public class MessageReactionAdded extends DLEvent {

	private final IUser user;
	private final IMessage message;
	private final Reaction reaction;

	public MessageReactionAdded(IMessage message, Reaction reaction, IUser user) {
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
	public Reaction getReaction() {
		return reaction;
	}

	/**
	 * @return the user
	 */
	public IUser getUser() {
		return user;
	}

}
