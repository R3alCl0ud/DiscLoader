/**
 * 
 */
package io.discloader.discloader.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.event.sharding.IShardingListener;
import io.discloader.discloader.common.registry.EntityRegistry;

/**
 * Class used to handling the shards of a sharded connection to Discord's
 * Gateway Usage: <code>
 * DLOptions options = new DLOptions();
 * 
 * ShardManager manager = new ShardManager(
 * </code>
 * 
 * @author Perry Berman
 * @since 0.0.3
 * @version 1
 */
public final class ShardManager {

	public static final Logger logger = new DLLogger(ShardManager.class).getLogger();
	public int shardCount = 1;

	public int launchedShards = 0;
	private String token;
	private List<Shard> shards;

	private List<IShardingListener> listeners;
	private Thread launcher;

	private DLOptions options;

	public ShardManager(DLOptions options) {
		this(options.token, options.shards);
		this.options = options;
	}

	public ShardManager(String token) {
		shards = new ArrayList<>();
		listeners = new ArrayList<>();
		this.token = token;
	}

	public ShardManager(String token, int totalShards) {
		this(token);
		setTotalShards(totalShards);
	}

	public void addShardingListener(IShardingListener... shardingListeners) {
		for (IShardingListener listener : shardingListeners) {
			listeners.add(listener);
		}
	}

	public void fireEvent(Shard shard) {
		for (IShardingListener listener : listeners)
			listener.ShardLaunched(shard);
	}

	public Shard getShard(int shard) {
		return shards.get(shard);
	}

	public List<Shard> getShards() {
		return shards;
	}

	public String getToken() {
		return token;
	}

	public void lanchShards(int totalShards) {
		setTotalShards(totalShards);
		logger.info(String.format("Shards: %d\n", shards.size()));
		launcher = new Thread("Sharding Manager") {

			public void run() {
				while (shards.size() < shardCount) {
					DLOptions options = new DLOptions().setToken(token).setSharding(shards.size(), shardCount).setPrefix(ShardManager.this.options.prefix);
					options.defaultCommands = ShardManager.this.options.defaultCommands;
					Shard shard = new Shard(options, ShardManager.this);
					shards.add(shard);
					EntityRegistry.addShard(shard);
					shard.launch();
					try {
						sleep(5500L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		launcher.setDaemon(false);
		launcher.start();
	}

	public void launchShards() {
		lanchShards(shardCount);
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	public void setTotalShards(int shards) {
		if (shards >= 1)
			shardCount = shards;
	}
}
