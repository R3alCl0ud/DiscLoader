package io.discloader.discloader.common.event.message;

import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.message.Message;

public class PrivateMessageCreateEvent extends MessageCreateEvent {

	public PrivateMessageCreateEvent(Message message) {
		super(message);
	}
	
	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) super.getChannel();
	}

}
