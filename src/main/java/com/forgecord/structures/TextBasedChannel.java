package main.java.com.forgecord.structures;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class TextBasedChannel {
	
	/**
	 * A {@link HashMap} containing the messages sent to this channel
	 */
	public HashMap<String, Message> messages;
	
	public TextBasedChannel() {
		this.messages = new HashMap<String, Message>();
	}
	
	public CompletableFuture<String> sendMessage() {
		return null;
	}
}
