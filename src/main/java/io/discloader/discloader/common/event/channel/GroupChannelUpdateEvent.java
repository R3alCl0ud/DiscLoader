package io.discloader.discloader.common.event.channel;

import java.util.HashMap;

import io.discloader.discloader.common.entity.channel.GroupChannel;
import io.discloader.discloader.entity.user.User;

public class GroupChannelUpdateEvent extends ChannelUpdateEvent {

	public GroupChannelUpdateEvent(GroupChannel channel, GroupChannel oldChannel) {
		super(channel, oldChannel);
	}

	@Override
	public GroupChannel getChannel() {
		return (GroupChannel) channel;
	}

	@Override
	public GroupChannel getOldChannel() {
		return (GroupChannel) oldChannel;
	}

	public HashMap<String, User> getRecipients() {
		return getChannel().getRecipients();
	}

}
