/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.IChannel;

/**
 * @author Perry Berman
 */
public class ChannelCreateEvent extends DLEvent {
	
	private final IChannel channel;
	
	public ChannelCreateEvent(IChannel channel2) {
		super(channel2.getLoader());
		this.channel = channel2;
	}
	
	public IChannel getChannel() {
		return channel;
	}
	
}
