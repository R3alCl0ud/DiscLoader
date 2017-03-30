package io.discloader.discloader.common.event.channel;

import java.util.Map;

import io.discloader.discloader.core.entity.channel.GroupChannel;
import io.discloader.discloader.entity.user.IUser;

public class GroupChannelCreateEvent extends ChannelCreateEvent {

	public GroupChannelCreateEvent(GroupChannel channel) {
		super(channel);
	}

	@Override
	public GroupChannel getChannel() {
		return (GroupChannel) super.getChannel();
	}

	public Map<String, IUser> getRecipients() {
		return getChannel().getRecipients();
	}

}
