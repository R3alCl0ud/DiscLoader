package io.discloader.discloader.entity.impl;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Message;
import io.discloader.discloader.util.ISnowflake;

/**
 * @author perryberman
 * @since 0.0.3
 */
public interface ITextChannel extends IChannel {

	Message getMessage(ISnowflake id);
	
	
	
	CompletableFuture<Message> sendMessage();
	
}
