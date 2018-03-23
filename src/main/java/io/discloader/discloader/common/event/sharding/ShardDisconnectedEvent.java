package io.discloader.discloader.common.event.sharding;

import io.discloader.discloader.common.Shard;

public class ShardDisconnectedEvent extends ShardEvent {

	public ShardDisconnectedEvent(Shard shard) {
		super(shard);
	}

}
