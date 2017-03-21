package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.message.Message;

/**
 * @author Perry Berman
 *
 */
public class MessageUpdateEvent extends DLEvent {

	private final Message message;

	private final Message oldMessage;

	public MessageUpdateEvent(Message message, Message oldMessage) {
		super(message.loader);

		this.message = message;

		this.oldMessage = oldMessage;
	}

	/**
	 * @return the oldMessage
	 */
	public Message getOldMessage() {
		return oldMessage;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

}
