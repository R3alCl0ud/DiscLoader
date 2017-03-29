package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.entity.user.User;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.Reaction;

public class MessageReactionAdded extends DLEvent {

	private final User user;
	private final Message message;
	private final Reaction reaction;

	public MessageReactionAdded(Message message, Reaction reaction, User user) {
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
	public User getUser() {
		return user;
	}

}
