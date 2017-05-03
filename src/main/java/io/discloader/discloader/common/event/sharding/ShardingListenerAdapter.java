package io.discloader.discloader.common.event.sharding;

import io.discloader.discloader.common.Shard;

public abstract class ShardingListenerAdapter implements IShardingListener {

	@Override
	public void ShardLaunched(Shard shard) {
		return;
	}

}
