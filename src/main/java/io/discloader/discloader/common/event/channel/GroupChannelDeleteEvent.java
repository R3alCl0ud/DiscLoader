package io.discloader.discloader.common.event.channel;

import java.util.HashMap;

import io.discloader.discloader.common.entity.channel.GroupChannel;
import io.discloader.discloader.common.entity.user.User;

public class GroupChannelDeleteEvent extends ChannelDeleteEvent {

	public GroupChannelDeleteEvent(GroupChannel channel) {
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
