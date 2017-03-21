/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.channels.Channel;

/**
 * 
 * 
 * @author Perry Berman
 */
public class ChannelUpdateEvent extends DLEvent {

	private final Channel channel;
	private final Channel oldChannel;
	public ChannelUpdateEvent(Channel channel, Channel oldChannel) {
		super(channel.loader);
		this.channel = channel;
		this.oldChannel = oldChannel;

	}
	
	public Channel getChannel() {
		return channel;
	}
	
	public Channel getOldChannel() {
		return oldChannel;
	}

}
