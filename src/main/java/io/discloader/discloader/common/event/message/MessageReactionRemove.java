package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.Reaction;
import io.discloader.discloader.entity.user.User;

public class MessageReactionRemove extends DLEvent {

	private final User user;
	private final Message message;
	private final Reaction reaction;

	public MessageReactionRemove(Message message, Reaction reaction, User user) {
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
	 * @return the reaction that was removed
	 */
	public Reaction getReaction() {
		return reaction;
	}

	/**
	 * @return the user that removed the reaction
	 */
	public User getUser() {
		return user;
	}

}
