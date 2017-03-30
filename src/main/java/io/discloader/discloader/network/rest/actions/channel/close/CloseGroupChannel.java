package io.discloader.discloader.network.rest.actions.channel.close;

import io.discloader.discloader.core.entity.channel.GroupChannel;

public class CloseGroupChannel extends CloseChannel<GroupChannel> {

	public CloseGroupChannel(GroupChannel channel) {
		super(channel);
	}

	@Override
	public GroupChannel getChannel() {
		return (GroupChannel) super.getChannel();
	}

}
