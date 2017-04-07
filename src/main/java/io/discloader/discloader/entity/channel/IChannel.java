package io.discloader.discloader.entity.channel;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents any channel on discord
 * 
 * @author Perry Berman
 * @see ISnowflake
 */
public interface IChannel extends ISnowflake {

	/**
	 * @return The type of the channel
	 */
	ChannelType getType();

	// ISnowflake getSnowflake();

	/**
	 * Whether or not the channel is a dm channel. Is always {@literal true} if
	 * {@link Channel#type type} is {@literal "groupDM"} or {@literal "dm"}
	 * 
	 * @return true if the channel is a dm/groupDM channel, false otherwise
	 * @author Perry Berman
	 */
	boolean isPrivate();

	/**
	 * Sets up the channel for use.
	 * 
	 * @param data Channel data.
	 */
	void setup(ChannelJSON data);

	/**
	 * @return A string that is in the correct format for mentioning this
	 *         channel in a {@link Message}
	 */
	String toMention();

	DiscLoader getLoader();

}
