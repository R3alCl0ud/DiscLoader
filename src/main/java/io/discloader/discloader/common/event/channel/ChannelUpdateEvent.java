/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channels.impl.Channel;

/**
 * @author Perry Berman
 */
public class ChannelUpdateEvent extends DLEvent {

	protected final Channel channel;
	protected final Channel oldChannel;

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
