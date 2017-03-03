package io.discloader.discloader.entity.channels;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.impl.IChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants.ChannelType;

public class Channel implements IChannel {

	public String id;

	public String name;

	protected ChannelType type;

	public final DiscLoader loader;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		this.type = null;

		if (data != null)
			this.setup(data);
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public ChannelType getType() {
		return this.type;
	}

	@Override
	public boolean isPrivate() {
		return this.type == ChannelType.TEXT || this.type == ChannelType.VOICE;
	}

	public void setup(ChannelJSON data) {
		this.id = data.id;

		if (data.name != null)
			this.name = data.name;

	}
}
