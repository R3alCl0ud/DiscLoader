package io.discloader.discloader.common.event.sharding;

public abstract class ShardingListenerAdapter implements IShardingListener {

	@Override
	public void onShardEvent(ShardEvent e) {
		if (e instanceof ShardLaunchedEvent)
			onShardLaunched((ShardLaunchedEvent) e);
		if (e instanceof ShardDisconnectedEvent)
			onShardDisconnected((ShardDisconnectedEvent) e);
	}

	@Override
	public void onShardLaunched(ShardLaunchedEvent e) {}

	@Override
	public void onShardDisconnected(ShardDisconnectedEvent e) {}

}
