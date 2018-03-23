package io.discloader.discloader.common.event.sharding;

import io.discloader.discloader.common.Shard;

public class ShardEvent {
	private final Shard shard;

	public ShardEvent(Shard shard) {
		this.shard = shard;
	}

	public Shard getShard() {
		return shard;
	}
	

}
