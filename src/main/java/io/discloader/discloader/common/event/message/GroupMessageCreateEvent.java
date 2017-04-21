package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;

public class GroupMessageCreateEvent extends MessageCreateEvent {

	public GroupMessageCreateEvent(IMessage message) {
		super(message);
	}

	@Override
	public IPrivateChannel getChannel() {
		return (IPrivateChannel) super.getChannel();
	}

}
