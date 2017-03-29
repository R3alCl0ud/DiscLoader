package io.discloader.discloader.common.event.channel;

import java.util.HashMap;

import io.discloader.discloader.common.entity.channel.GroupChannel;
import io.discloader.discloader.entity.user.User;

public class GroupChannelCreateEvent extends ChannelCreateEvent {

	public GroupChannelCreateEvent(GroupChannel channel) {
		super(channel);
	}

	@Override
	public GroupChannel getChannel() {
		return (GroupChannel) super.getChannel();
	}

	public HashMap<String, User> getRecipients() {
		return getChannel().getRecipients();
	}

}
