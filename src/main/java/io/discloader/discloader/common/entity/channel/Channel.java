package io.discloader.discloader.common.entity.channel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents any channel on discord
 * 
 * @author Perry Berman
 */
public class Channel implements IChannel {

	/**
	 * The channel's Snowflake ID.
	 */
	private String id;

	protected ChannelType type;

	/**
	 * The current instance of DiscLoader
	 */
	protected final DiscLoader loader;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		id = data.id;

		type = ChannelType.CHANNEL;

		if (data != null) setup(data);
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	@Override
	public ChannelType getType() {
		return type;
	}

	@Override
	public boolean isPrivate() {
		return type != ChannelType.TEXT && type != ChannelType.VOICE && type != ChannelType.CHANNEL;
	}

	public void setup(ChannelJSON data) {

	}

	public String toMention() {
		return String.format("<#%s>", id);
	}
}
