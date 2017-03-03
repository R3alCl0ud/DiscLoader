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
	 * 
	 * @return A HashMap of cached messages
	 */
	HashMap<String, Message> getMessages();

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param embed The embed to send
	 * @return A Future that completes with a {@link Message} if successful,
	 */
	CompletableFuture<Message> sendEmbed(RichEmbed embed);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param content The message's content
	 * @return A Future that completes with a {@link Message} if successful,
	 */
	CompletableFuture<Message> sendMessage(String content);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param content The message's content
	 * @param embed The RichEmbed to send with the message
	 * @return A Future that completes with a {@link Message} if successful.
	 */
	CompletableFuture<Message> sendMessage(String content, RichEmbed embed);
}
