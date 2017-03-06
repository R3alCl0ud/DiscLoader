package io.discloader.discloader.entity.impl;

import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants.ChannelType;

public interface IChannel {

	String getID();

	
	
	/**
	 * @return The type of the channel
	 */
	ChannelType getType();

	/**
	 * Whether or not the channel is a dm channel. Is always {@literal true} if
	 * {@link Channel#type type} is {@literal "groupDM"} or {@literal "dm"}
	 * 
	 * @author Perry Berman
	 */
	boolean isPrivate();
	/**
	 * Sets up the channel for use.
	 * @param data Channel data.
	 */
	void setup(ChannelJSON data);
}
