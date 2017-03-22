/**
 * 
 */
package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;

/**
 * The object passed to the {@literal "MessageCreate"} eventHandler
 * 
 * @author Perry Berman
 * @since 0.0.1_Alpha
 * @see Message
 * @see DiscLoader
 */
public class MessageCreateEvent extends DLEvent {

	/**
	 * The new message received from the gateway.
	 */
	private final Message message;

	/**
	 * The messages arguments.
	 */
	public final String[] args;

	public MessageCreateEvent(Message message) {
		super(message.loader);
		this.message = message;
		this.args = this.message.content.split(" ");
	}

	public Message getMessage() {
		return message;
	}
	
	public ITextChannel getChannel() {
		return message.channel;
	}

}
