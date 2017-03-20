package io.discloader.discloader.network.gateway.packets.request;

public class GatewayIdentify {
	public String token;
	public int large_threshold;
	public int[] shard = null;
	public Object properties;

	public GatewayIdentify(String token, int largeThreshold, Object properties) {
		this.token = token;
		this.large_threshold = largeThreshold;
		this.properties = properties;
	}

	public void setShard(int s, int ts) {
		shard = new int[2];
		shard[0] = s;
		shard[1] = ts;
	}
}
