package io.discloader.discloader.common.event.channel;

import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.user.IUser;

public class PrivateChannelDeleteEvent extends ChannelDeleteEvent {
	
	public PrivateChannelDeleteEvent(PrivateChannel channel) {
		super(channel);
	}
	
	@Override
	public IPrivateChannel getChannel() {
		return (IPrivateChannel) super.getChannel();
	}
	
	public IUser getRecipient() {
		return getChannel().getRecipient();
	}
	
}
