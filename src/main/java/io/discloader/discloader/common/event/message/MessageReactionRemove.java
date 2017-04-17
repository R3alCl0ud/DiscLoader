package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.message.Reaction;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;

public class MessageReactionRemove extends DLEvent {

	private final IUser user;
	private final IMessage message;
	private final Reaction reaction;

	public MessageReactionRemove(IMessage message, Reaction reaction, IUser user) {
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
	public Reaction getReaction() {
		return reaction;
	}

	/**
	 * @return the user that removed the reaction
	 */
	public IUser getUser() {
		return user;
	}

}
