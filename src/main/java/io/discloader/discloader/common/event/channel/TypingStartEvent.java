package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.user.User;

/**
 * Object passed to {@link IEventListener#TypingStart(TypingStartEvent)}
 * handlers
 * 
 * @author Perry Berman
 */
public class TypingStartEvent extends DLEvent {

	private User user;
	private ITextChannel channel;

	public TypingStartEvent(User user, ITextChannel channel) {
		super(user.loader);
		this.user = user;
		this.channel = channel;
	}

	/**
	 * @return the channel that the {@link #getUser() user} is typing in.
	 */
	public ITextChannel getChannel() {
		return channel;
	}

	/**
	 * @return the user that started typing.
	 */
	public User getUser() {
		return user;
	}

}
