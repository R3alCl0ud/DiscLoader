package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.IChannel;

/**
 * @author Perry Berman
 */
public class ChannelDeleteEvent extends DLEvent {
	
	private final IChannel channel;
	
	public ChannelDeleteEvent(IChannel channel2) {
		super(channel2.getLoader());
		
		this.channel = channel2;
	}
	
	/**
	 * Returns the channel that was deleted
	 * 
	 * @return The deleted channel
	 */
	public IChannel getChannel() {
		return channel;
	}
	
}
