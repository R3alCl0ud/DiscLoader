package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.IMentionable;

/**
 * @author Perry Berman
 */
public interface IGuildTextChannel extends IGuildChannel, ITextChannel, IMentionable {

	@Override
	CompletableFuture<IGuildTextChannel> edit(String name, int position);

	CompletableFuture<IGuildTextChannel> edit(String name, String topic);

	CompletableFuture<IGuildTextChannel> edit(String name, String topic, int position);

	boolean isNSFW();

	CompletableFuture<IGuildTextChannel> setTopic(String topic);

	CompletableFuture<IGuildTextChannel> setNSFW(boolean nsfw);
}
