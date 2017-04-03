/**
 * 
 */
package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;

/**
 * @author Perry Berman
 *
 */
public class MessageDeleteEvent extends DLEvent {

	private final IMessage message;
	
	public MessageDeleteEvent(IMessage message) {
		super(message.getLoader());
		
		this.message = message;
	}

	public IMessage getMessage() {
		return message;
	}
	
	public ITextChannel getChannel() {
		return message.getChannel();
	}
	
}
