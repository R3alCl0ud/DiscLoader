/**
 * 
 */
package io.discloader.discloader.common;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.event.sharding.ShardLaunchedEvent;

/**
 * 
 * 
 * @author Perry Berman
 */
public final class Shard {

	private final DLOptions options;

	private DiscLoader loader;

	private final ShardManager manager;

	public Shard(DLOptions options, ShardManager manager) {
		this.options = options;
		this.manager = manager;
	}

	/**
	 * @return the shardID
	 */
	public int getShardID() {
		return options.shard;
	}

	public int getShardCount() {
		return manager.shardCount;
	}

	public CompletableFuture<Shard> launch() {
		CompletableFuture<Shard> future = new CompletableFuture<>();
		loader = new DiscLoader(this);
		loader.setOptions(options);
		CompletableFuture<DiscLoader> cf = loader.login();
		cf.thenAcceptAsync(ignored -> {
			future.complete(Shard.this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		manager.fireEvent(new ShardLaunchedEvent(this));
		return future;
	}

	/**
	 * @return The shard's {@link DiscLoader} instance.
	 */
	public DiscLoader getLoader() {
		return loader;
	}

	/**
	 * @return the manager
	 */
	public ShardManager getManager() {
		return manager;
	}

}