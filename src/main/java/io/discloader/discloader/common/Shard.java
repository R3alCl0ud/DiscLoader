/**
 * 
 */
package io.discloader.discloader.common;

import java.util.concurrent.CompletableFuture;

/**
 * @author Perry Berman
 */
public class Shard {

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

	public CompletableFuture<Shard> launch() {
		CompletableFuture<Shard> future = new CompletableFuture<>();
		future.thenAcceptAsync(action -> manager.fireEvent(this));
		loader = new DiscLoader(options);
		loader.login().thenAcceptAsync(l -> future.complete(this));
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