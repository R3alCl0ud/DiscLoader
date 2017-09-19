package io.discloader.discloader.entity.channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.IOverwrite;

public interface IChannelCategory extends IGuildChannel {

	/**
	 * Adds an {@link IGuildChannel} to this category.
	 * 
	 * @param <T> The type of the {@link IGuildChannel channel}(type must
	 *            be a sub type of {@link IGuildChannel}) being
	 *            added to
	 *            this category.
	 * @param channel The {@link IGuildChannel channel} to add to this category.
	 * @return A CompletableFuture that completes with the added
	 *         {@link IGuildChannel channel} if successful.
	 */
	public <T extends IGuildChannel> CompletableFuture<T> addChannel(T channel);

	public CompletableFuture<IGuildChannel> createChannel(String name, ChannelTypes type);

	public CompletableFuture<IGuildChannel> createChannel(String name, ChannelTypes type, IOverwrite... overwrites);

	public CompletableFuture<IGuildTextChannel> createTextChannel(String name);

	public CompletableFuture<IGuildTextChannel> createTextChannel(String name, IOverwrite... overwrites);

	public CompletableFuture<IGuildVoiceChannel> createVoiceChannel(String name);

	public CompletableFuture<IGuildVoiceChannel> createVoiceChannel(String name, IOverwrite... overwrites);

	public Map<Long, IGuildChannel> getChannels();

	Map<Long, IGuildTextChannel> getTextChannels();

	Map<Long, IGuildVoiceChannel> getVoiceChannels();

	public <T extends IGuildChannel> CompletableFuture<T> removeChannel(T guildChannel);
}
