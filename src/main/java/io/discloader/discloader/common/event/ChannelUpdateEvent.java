/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.channels.Channel;

/**
 * @author Perry Berman
 *
 */
public class ChannelUpdateEvent extends DLEvent {

	public final Channel channel;

	public ChannelUpdateEvent(Channel channel) {
		super(channel.loader);
		this.channel = channel;

	}

}
