/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Message;

/**
 * @author Perry Berman
 *
 */
public class MessageDeleteEvent extends DLEvent {

	public final Message message;
	
	public MessageDeleteEvent(Message message) {
		super(message.loader);
		
		this.message = message;
	}

}
