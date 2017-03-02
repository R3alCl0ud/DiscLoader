package io.discloader.discloader.entity.impl;

import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants.ChannelType;

public interface IChannel {
	
	String getID();
	
	ChannelType getType();

	boolean isPrivate();
	
	void setup(ChannelJSON data);
}
