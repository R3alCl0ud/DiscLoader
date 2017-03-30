/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.entity.channel.IChannel;

/**
 * @author Perry Berman
 */
public class ChannelCreateEvent extends DLEvent {

	private final Channel channel;

	public ChannelCreateEvent(Channel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public IChannel getChannel() {
		return channel;
	}

}
