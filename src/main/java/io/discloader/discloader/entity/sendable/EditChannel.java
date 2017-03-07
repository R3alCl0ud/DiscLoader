package io.discloader.discloader.entity.sendable;

public class EditChannel {
	public String name;
	public String topic;

	public int position;
	public int bitrate;
	public int user_limit;

	public EditChannel(String name, String topic, int position, int bitrate, int userLimit) {
		this.name = name;
		this.topic = topic;
		this.position = position;
		this.bitrate = bitrate;
		this.user_limit = userLimit;
	}
}
