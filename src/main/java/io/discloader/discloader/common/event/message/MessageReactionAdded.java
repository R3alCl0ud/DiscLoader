package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.Reaction;
import io.discloader.discloader.entity.user.IUser;

public class MessageReactionAdded extends DLEvent {

	private final IUser user;
	private final Message message;
	private final Reaction reaction;

	public MessageReactionAdded(Message message, Reaction reaction, IUser user) {
		super(message.loader);
		this.message = message;
		this.reaction = reaction;
		this.user = user;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
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
