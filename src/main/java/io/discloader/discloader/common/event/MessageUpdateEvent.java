package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Message;

/**
 * @author Perry Berman
 *
 */
public class MessageUpdateEvent extends DLEvent {

	public final Message message;
	
	public MessageUpdateEvent(Message message) {
		super(message.loader);

		this.message = message;
	}

}
