package io.discloader.discloader.common.event.message;

import io.discloader.discloader.common.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.message.Message;

public class PrivateMessageCreateEvent extends MessageCreateEvent {

	public PrivateMessageCreateEvent(Message message) {
		super(message);
	}
	
	@Override
	public PrivateChannel getChannel() {
		return (PrivateChannel) super.getChannel();
	}

}
