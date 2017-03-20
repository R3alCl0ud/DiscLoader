package io.discloader.discloader.entity.message;

import io.discloader.discloader.entity.impl.ITextChannel;

/**
 * Options for fetching messages from {@link ITextChannel ITextChannels}
 * 
 * @author Perry Berman
 */
public class MessageFetchOptions {
	public String around;
	public String before;
	public String after;
	public int limit;

	public MessageFetchOptions() {
		limit = 50;
		this.around = "";
		this.after = "";
		this.before = "";
	}

	public MessageFetchOptions setAfter(Message after) {
		this.after = after.id;
		return this;
	}

	public MessageFetchOptions setAfter(String after) {
		this.after = after;
		return this;
	}

	public MessageFetchOptions setAround(Message around) {
		this.around = around.id;
		return this;
	}

	public MessageFetchOptions setAround(String around) {
		this.around = around;
		return this;
	}

	public MessageFetchOptions setBefore(Message before) {
		this.before = before.id;
		return this;
	}

	public MessageFetchOptions setBefore(String before) {
		this.before = before;
		return this;
	}

	/**
	 * @param limit the limit to set
	 */
	public MessageFetchOptions setLimit(int limit) {
		this.limit = limit;
		return this;
	}

}
