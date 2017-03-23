package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channels.Channel;

/**
 * @author Perry Berman
 */
public class ChannelDeleteEvent extends DLEvent {

	private final Channel channel;

	public ChannelDeleteEvent(Channel channel) {
		super(channel.loader);

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
