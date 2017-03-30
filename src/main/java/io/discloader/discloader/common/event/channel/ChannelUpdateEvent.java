/**
 * 
 */
package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.IChannel;

/**
 * @author Perry Berman
 */
public class ChannelUpdateEvent extends DLEvent {
	
	protected final IChannel channel;
	protected final IChannel oldChannel;
	
	public ChannelUpdateEvent(IChannel channel, IChannel oldChannel) {
		super(channel.getLoader());
		this.channel = channel;
		this.oldChannel = oldChannel;
	}
	
	public IChannel getChannel() {
		return channel;
	}
	
	public IChannel getOldChannel() {
		return oldChannel;
	}
	
}
