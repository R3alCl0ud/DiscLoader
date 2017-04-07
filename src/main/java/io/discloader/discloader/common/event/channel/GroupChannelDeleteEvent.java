package io.discloader.discloader.common.event.channel;

import java.util.Map;

import io.discloader.discloader.core.entity.channel.GroupChannel;
import io.discloader.discloader.entity.user.IUser;

public class GroupChannelDeleteEvent extends ChannelDeleteEvent {

	public GroupChannelDeleteEvent(GroupChannel channel) {
		super(channel);
	}

	@Override
	public GroupChannel getChannel() {
		return (GroupChannel) super.getChannel();
	}

	public Map<Long, IUser> getRecipients() {
		return getChannel().getRecipients();
	}

}
