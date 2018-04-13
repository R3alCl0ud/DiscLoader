package io.discloader.discloader.entity.channel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.network.json.ChannelJSON;

/**
 * Represents any channel on discord
 * 
 * @author Perry Berman
 * @see ISnowflake
 */
// @FunctionalInterface
public interface IChannel extends ISnowflake, ICreationTime, IMentionable {

	/**
	 * @return The type of the channel
	 */
	ChannelTypes getType();

	/**
	 * Whether or not the channel is a {@link ChannelTypes#DM DM} or
	 * {@link ChannelTypes#GROUP Group DM} channel.
	 * 
	 * @return {@code true} if the channel is a {@link ChannelTypes#DM DM} or
	 *         {@link ChannelTypes#GROUP Group DM} channel, {@code false} otherwise
	 * 
	 */
	boolean isPrivate();

	/**
	 * Sets up the channel for use.
	 * 
	 * @param data
	 *            Channel data.
	 */
	void setup(ChannelJSON data);

	/**
	 * @return The instance of {@link DiscLoader} that was used to create the
	 *         {@link IChannel} object.
	 */
	DiscLoader getLoader();

}
