/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.entity.channel.Channel;
import io.discloader.discloader.common.event.DLEvent;

/**
 * @author Perry Berman
 */
public class ChannelCreateEvent extends DLEvent {

	private final Channel channel;

	public ChannelCreateEvent(Channel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

}
