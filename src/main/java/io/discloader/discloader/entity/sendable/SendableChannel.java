package io.discloader.discloader.entity.sendable;

import io.discloader.discloader.network.json.OverwriteJSON;

public class SendableChannel {

	public String name, topic, type;
	public int user_limit, position, bitrate;
	public OverwriteJSON[] permission_overwrites;

}
