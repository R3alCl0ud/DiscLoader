package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

public interface IChannelCategory extends IGuildChannel {

	public <T extends IGuildChannel> CompletableFuture<T> addChannel(T guildChannel);
}
