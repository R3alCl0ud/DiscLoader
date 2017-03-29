/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.entity.channel.Channel;
import io.discloader.discloader.common.event.DLEvent;

/**
 * @author Perry Berman
 */
public class ChannelUpdateEvent extends DLEvent {

	protected final Channel channel;
	protected final Channel oldChannel;

	public ChannelUpdateEvent(Channel channel, Channel oldChannel) {
		super(channel.getLoader());
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
