package io.discloader.discloader.common.event.channel;

import java.util.Map;

import io.discloader.discloader.core.entity.channel.GroupChannel;
import io.discloader.discloader.entity.user.IUser;

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

	public Map<Long, IUser> getRecipients() {
		return getChannel().getRecipients();
	}

}
