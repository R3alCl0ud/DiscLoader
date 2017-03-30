package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.user.IUser;

public class PrivateChannelUpdateEvent extends ChannelUpdateEvent {

	public PrivateChannelUpdateEvent(PrivateChannel channel, PrivateChannel oldChannel) {
		super(channel, oldChannel);
	}


	@Override
	public IPrivateChannel getOldChannel() {
		return (IPrivateChannel) oldChannel;
	}

	@Override
	public IPrivateChannel getChannel() {
		return (IPrivateChannel) super.getChannel();
	}
	
	public IUser getRecipient() {
		return getChannel().getRecipient();
	}

}
