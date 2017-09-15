/**
 * 
 */
package io.discloader.discloader.common;

import java.util.concurrent.CompletableFuture;

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
		loader = new DiscLoader(this);
		loader.setOptions(options);
		loader.login();
		manager.fireEvent(this);
		return CompletableFuture.completedFuture(this);
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