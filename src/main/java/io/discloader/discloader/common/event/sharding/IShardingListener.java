package io.discloader.discloader.common.event.sharding;

import io.discloader.discloader.common.Shard;

public interface IShardingListener {

	void ShardLaunched(Shard shard);

}
