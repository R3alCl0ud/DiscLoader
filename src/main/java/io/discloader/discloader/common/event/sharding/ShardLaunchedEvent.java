package io.discloader.discloader.common.event.sharding;

import io.discloader.discloader.common.Shard;

public class ShardLaunchedEvent extends ShardEvent {

	public ShardLaunchedEvent(Shard shard) {
		super(shard);
	}

}
