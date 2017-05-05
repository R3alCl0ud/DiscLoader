/**
 * 
 */
package io.discloader.discloader.common;

import java.util.ArrayList;
import java.util.List;

import io.discloader.discloader.common.event.sharding.IShardingListener;

/**
 * @author Perry Berman
 * @since 0.0.3
 */
public class ShardManager {

	public int shardCount = 1;
	public int launchedShards = 0;

	private String token;
	private List<Shard> shards;
	private List<IShardingListener> listeners;

	private Thread launcher;

	public void addShardingListener(IShardingListener... shardingListeners) {
		for (IShardingListener listener : shardingListeners) {
			listeners.add(listener);
		}
	}

	public ShardManager(DLOptions options) {
		this(options.token, options.shards);
		System.out.println(options.shards);
	}

	public ShardManager(String token) {
		shards = new ArrayList<>();
		listeners = new ArrayList<>();
		this.token = token;
	}

	public ShardManager(String token, int totalShards) {
		this(token);
		setTotalShards(totalShards);
		// ShardManager m = new ShardManager(new DLOptions);
	}

	public String getToken() {
		return token;
	}

	public Shard getShard(int shard) {
		return shards.get(shard);
	}

	public List<Shard> getShards() {
		return shards;
	}

	public void lanchShards(int totalShards) {
		setTotalShards(totalShards);

		launcher = new Thread("Sharding Manager") {

			public void run() {
				while (shards.size() < shardCount) {
					System.out.printf("Shards: %d\n", shards.size());
					DLOptions options = new DLOptions().setToken(token).setSharding(shards.size(), shardCount);
					Shard shard = new Shard(options, ShardManager.this);
					shards.add(shard);
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

	public void fireEvent(Shard shard) {
		for (IShardingListener listener : listeners)
			listener.ShardLaunched(shard);
	}

	public void launchShards() {
		lanchShards(shardCount);
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	public void setTotalShards(int shards) {
		if (shards >= 1) shardCount = shards;
	}
}
