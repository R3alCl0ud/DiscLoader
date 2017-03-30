package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;

/**
 * @author Perry Berman
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

	/**
	 * Gets the message's channel
	 * 
	 * @return the {@link #getMessage() message}'s {@link ITextChannel}
	 */
	public ITextChannel getChannel() {
		return message.channel;
	}

}
