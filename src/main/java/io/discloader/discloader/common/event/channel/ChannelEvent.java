package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.IChannel;

public class ChannelEvent extends DLEvent {

	protected final IChannel channel;

	public ChannelEvent(IChannel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public IChannel getChannel() {
		return channel;
	}

}
