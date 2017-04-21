package io.discloader.discloader.common.event.message;

import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;

public class PrivateMessageUpdateEvent extends MessageUpdateEvent {

	public PrivateMessageUpdateEvent(IMessage message, IMessage oldMessage) {
		super(message, oldMessage);
	}

	@Override
	public IPrivateChannel getChannel() {
		return (IPrivateChannel) super.getChannel();
	}
}
