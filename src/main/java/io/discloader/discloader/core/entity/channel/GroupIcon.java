package io.discloader.discloader.core.entity.channel;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.network.util.Endpoints;

/**
 * @author Perry Berman
 *
 */
public class GroupIcon implements IIcon {
	
	private final IGroupChannel channel;
	
	public GroupIcon(IGroupChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public String getHash() {
		return channel.getIconHash();
	}
	
	@Override
	public String toString() {
		return getHash() != null ? Endpoints.channelIcon(channel.getID(), getHash()) : Endpoints.defaultGroupIcon;
	}
	
	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(toString());
	}
	
}
