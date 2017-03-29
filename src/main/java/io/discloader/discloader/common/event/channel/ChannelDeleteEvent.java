package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.entity.channel.Channel;
import io.discloader.discloader.common.event.DLEvent;

/**
 * @author Perry Berman
 */
public class ChannelDeleteEvent extends DLEvent {

	private final Channel channel;

	public ChannelDeleteEvent(Channel channel) {
		super(channel.getLoader());

		this.channel = channel;
	}

	/**
	 * Returns the channel that was deleted
	 * 
	 * @return The deleted channel
	 */
	public Channel getChannel() {
		return channel;
	}

}
