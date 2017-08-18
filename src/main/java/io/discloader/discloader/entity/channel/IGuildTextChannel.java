package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.IMentionable;

/**
 * @author Perry Berman
 */
public interface IGuildTextChannel extends IGuildChannel, ITextChannel, IMentionable {
	
	CompletableFuture<IGuildTextChannel> setTopic(String topic);
	
	boolean isNSFW();
	
	// CompletableFuture<IGuildTextChannel> setNSFW(boolean nsfw);
}
