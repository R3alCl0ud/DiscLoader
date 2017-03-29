package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.common.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.user.User;

public class PrivateChannelDeleteEvent extends ChannelDeleteEvent {

	public PrivateChannelDeleteEvent(PrivateChannel channel) {
		super(channel);
	}

	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) super.getChannel();
	}

	public User getRecipient() {
		return getChannel().recipient;
	}

}
