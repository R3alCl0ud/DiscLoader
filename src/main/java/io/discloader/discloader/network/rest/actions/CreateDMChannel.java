package io.discloader.discloader.network.rest.actions;

import io.discloader.discloader.entity.channels.PrivateChannel;
import io.discloader.discloader.entity.user.User;

public class CreateDMChannel extends RESTAction<PrivateChannel> {

	public CreateDMChannel(User user) {
		super(user.loader);
	}

}
