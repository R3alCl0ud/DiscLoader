package io.discloader.discloader.network.json;

public class TypingStartJSON {

	/**
	 * id of the user
	 */
	public String user_id;

	/**
	 * id of the channel
	 */
	public String channel_id;

	/**
	 * unix time (in seconds) of when the user started typing
	 */
	public int timestamp;
}
