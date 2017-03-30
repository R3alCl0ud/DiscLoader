/**
 * 
 */
package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;

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
	private final IMessage message;

	/**
	 * The messages arguments.
	 */
	public final String[] args;

	public MessageCreateEvent(IMessage message) {
		super(message.getLoader());
		this.message = message;
		this.args = this.message.getContent().split(" ");
	}

	public IMessage getMessage() {
		return message;
	}

	public ITextChannel getChannel() {
		return message.getChannel();
	}

}
