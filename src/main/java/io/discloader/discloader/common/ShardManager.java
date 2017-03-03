/**
 * 
 */
package io.discloader.discloader.common;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * 
 * @author Perry Berman
 * @since 0.0.3
 */
public class ShardManager {

	/**
	 * A HashMap of shard processes
	 */
	public final HashMap<Integer, Shard> shards;

	public int shardCount = 1;
	public int launchedShards = 0;

	public ShardManager(String shard) {
		this(shard, 1);
	}

	public ShardManager(String shard, int shardCount) {
		this.shards = new HashMap<>();
	}

	public void start() {
		launchShard(0);
	}

	public void start(int shardCount) {
		this.shardCount = shardCount;
		launchShard(0);
	}

	protected void launchShard(final int id) {
		if (launchedShards == this.shardCount) {
			return;
		}
		Shard shard = new Shard(id, shardCount);
		this.shards.put(id, shard);
		shard.execute();
		launchedShards++;
		TimerTask launch = new TimerTask() {

			@Override
			public void run() {
				launchShard(id + 1);
			}
		};

		new Timer().schedule(launch, 5000);
	}

}
