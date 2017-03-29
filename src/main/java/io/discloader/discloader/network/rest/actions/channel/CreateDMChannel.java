package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.entity.channel.PrivateChannel;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class CreateDMChannel extends RESTAction<PrivateChannel> {

	private User user;

	public class dm {

		public String recipient_id;

		public dm(String r) {
			recipient_id = r;
		}
	}

	public CreateDMChannel(User user) {
		super(user.loader);
	}

	public CompletableFuture<PrivateChannel> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.currentUserChannels, Methods.POST, true, new dm(user.getID())));
	}

	public void complete(String packet, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		ChannelJSON data = gson.fromJson(packet, ChannelJSON.class);
		future.complete((PrivateChannel) loader.addChannel(data));
	}

}
