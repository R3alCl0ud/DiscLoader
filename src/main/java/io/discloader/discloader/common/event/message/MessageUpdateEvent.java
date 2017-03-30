package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;

/**
 * @author Perry Berman
 */
public class MessageUpdateEvent extends DLEvent {

	private final IMessage message;

	private final IMessage oldMessage;

	public MessageUpdateEvent(IMessage message, IMessage oldMessage) {
		super(message.getLoader());

		this.message = message;

		this.oldMessage = oldMessage;
	}

	/**
	 * @return the oldMessage
	 */
	public IMessage getOldMessage() {
		return oldMessage;
	}

	/**
	 * @return the message
	 */
	public IMessage getMessage() {
		return message;
	}

	/**
	 * Gets the message's channel
	 * 
	 * @return the {@link #getMessage() message}'s {@link ITextChannel}
	 */
	public ITextChannel getChannel() {
		return message.getChannel();
	}

}
