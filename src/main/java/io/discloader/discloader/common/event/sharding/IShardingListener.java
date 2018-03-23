package io.discloader.discloader.common.event.sharding;

public interface IShardingListener {

	void onShardEvent(ShardEvent e);

	void onShardLaunched(ShardLaunchedEvent e);

	void onShardDisconnected(ShardDisconnectedEvent e);

}
