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

	Message getMessage(String id);
	
	HashMap<String, Message> getMessages();
	
	CompletableFuture<Message> sendMessage(String content);

	CompletableFuture<Message> sendMessage(String content, RichEmbed embed);

	CompletableFuture<Message> sendEmbed(RichEmbed embed);
}
