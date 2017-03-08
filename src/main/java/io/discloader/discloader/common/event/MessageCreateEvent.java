/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.message.Message;

/**
 * The object passed to the {@literal "MessageCreate"} eventHandler
 * @author Perry Berman
 * @since 0.0.1_Alpha
 * @see Message
 * @see DiscLoader
 */
public class MessageCreateEvent {

	/**
	 * The current instance of the loader.
	 */
	public final DiscLoader loader;
	
	/**
	 * The new message received from the gateway.
	 */
	public final Message message;
	
	/**
	 * The messages arguments.
	 */
	public final String[] args;
	
	public MessageCreateEvent(Message message) {
		this.message = message;
		this.loader = this.message.loader;
		this.args = this.message.content.split(" ");
	}

}
