package io.discloader.discloader.entity.impl;

import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants.ChannelType;

public interface IChannel {
	
	String getID();
	
	void setup(ChannelJSON data);

	boolean isPrivate();
	
	ChannelType getType();
}
