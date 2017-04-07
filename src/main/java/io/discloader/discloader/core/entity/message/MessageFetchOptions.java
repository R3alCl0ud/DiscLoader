package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;

/**
 * Options for fetching messages from {@link ITextChannel ITextChannels}
 * 
 * @author Perry Berman
 */
public class MessageFetchOptions {

	public long around;
	public long before;
	public long after;
	public int limit;

	public MessageFetchOptions() {
		limit = 50;
		// around;
		// after = "";
		// before = "";
	}

	public MessageFetchOptions setAfter(IMessage after) {
		this.after = after.getID();
		return this;
	}

	public MessageFetchOptions setAfter(long after) {
		this.after = after;
		return this;
	}

	public MessageFetchOptions setAround(IMessage around) {
		this.around = around.getID();
		return this;
	}

	public MessageFetchOptions setAround(long around) {
		this.around = around;
		return this;
	}

	public MessageFetchOptions setBefore(IMessage before) {
		this.before = before.getID();
		return this;
	}

	public MessageFetchOptions setBefore(long before) {
		this.before = before;
		return this;
	}

	/**
	 * Sets the limit of how many messages to fetch
	 * 
	 * @param limit the limit to set
	 * @return {@code this}
	 */
	public MessageFetchOptions setLimit(int limit) {
		this.limit = limit;
		return this;
	}

}
