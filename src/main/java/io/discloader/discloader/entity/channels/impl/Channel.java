package io.discloader.discloader.entity.channels.impl;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Snowflake;
import io.discloader.discloader.entity.channels.IChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;
import io.discloader.discloader.util.ISnowflake;

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

	private Snowflake snowflake;

	protected ChannelType type;

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		type = ChannelType.CHANNEL;

		if (data != null) setup(data);
	}

	@Override
	public String getID() {
		return snowflake.toString();
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	@Override
	public ISnowflake getSnowflake() {
		return snowflake;
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
		id = data.id;

		snowflake = new Snowflake(id);
	}

	public String toMention() {
		return String.format("<#%s>", id);
	}
}
