package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class CreateDMChannel extends RESTAction<IPrivateChannel> {

	private IUser user;

	public class dm {

		public String recipient_id;

		public dm(String r) {
			recipient_id = r;
		}
	}

	public CreateDMChannel(IUser user) {
		super(user.getLoader());
	}

	public CompletableFuture<IPrivateChannel> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.currentUserChannels, Methods.POST, true, new dm(Long.toUnsignedString(user.getID()))));
	}

	public void complete(String packet, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		ChannelJSON data = gson.fromJson(packet, ChannelJSON.class);
		future.complete((IPrivateChannel) EntityRegistry.addChannel(data, loader));
	}

}
