/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channels.Channel;

/**
 * @author Perry Berman
 */
public class ChannelCreateEvent extends DLEvent {

	private final Channel channel;

	public ChannelCreateEvent(Channel channel) {
		super(channel.loader);
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

}
