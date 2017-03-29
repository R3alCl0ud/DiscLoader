package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.entity.channel.PrivateChannel;
import io.discloader.discloader.common.entity.user.User;

public class PrivateChannelUpdateEvent extends ChannelUpdateEvent {

	public PrivateChannelUpdateEvent(PrivateChannel channel, PrivateChannel oldChannel) {
		super(channel, oldChannel);
	}

	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) channel;
	}

	@Override
	public PrivateChannel getOldChannel() {
		return (PrivateChannel) oldChannel;
	}

	public User getRecipient() {
		return getChannel().recipient;
	}

}
