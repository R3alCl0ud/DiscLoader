package io.discloader.discloader.entity.channels.impl;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channels.IChannel;
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
	public final DiscLoader loader;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		type = ChannelType.CHANNEL;

		if (data != null) setup(data);
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public ChannelType getType() {
		return type;
	}

	@Override
	public boolean isPrivate() {
		return this.type != ChannelType.TEXT && this.type != ChannelType.VOICE;
	}

	public void setup(ChannelJSON data) {
		id = data.id;
	}

	public String toMention() {
		return String.format("<#%s>", id);
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}
}
