package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.entity.message.IMessage;

public class GroupMessageUpdateEvent extends MessageUpdateEvent {

	public GroupMessageUpdateEvent(IMessage message, IMessage oldMessage) {
		super(message, oldMessage);
	}

	@Override
	public IGroupChannel getChannel() {
		return (IGroupChannel) super.getChannel();
	}
}
