package io.discloader.discloader.entity.impl;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.sendable.RichEmbed;

/**
 * @author perryberman
 * @since 0.0.3
 */
public interface ITextChannel extends IChannel {

	/**
	 * @param id The Snowflake ID of the message
	 * @return The cached message, or null if no message was found
	 */
	Message getMessage(String id);
	
	/**
	 * Gets the channels cached messages
	 * @return A HashMap of cached messages
	 */
	HashMap<String, Message> getMessages();
	
	CompletableFuture<Message> sendEmbed(RichEmbed embed);

	CompletableFuture<Message> sendMessage(String content);

	CompletableFuture<Message> sendMessage(String content, RichEmbed embed);
}
