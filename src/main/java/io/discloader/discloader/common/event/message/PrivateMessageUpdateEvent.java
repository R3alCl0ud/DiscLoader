package io.discloader.discloader.common.event.message;

import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.message.Message;

public class PrivateMessageUpdateEvent extends MessageUpdateEvent {

	public PrivateMessageUpdateEvent(Message message, Message oldMessage) {
		super(message, oldMessage);
	}

	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) super.getChannel();
	}
}
