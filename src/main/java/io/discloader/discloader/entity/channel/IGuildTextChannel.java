package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

/**
 * @author Perry Berman
 */
public interface IGuildTextChannel extends IGuildChannel, ITextChannel {

	CompletableFuture<IGuildTextChannel> setTopic(String topic);
}
