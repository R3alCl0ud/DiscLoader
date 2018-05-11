package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class CreateDMChannel extends RestAction<IPrivateChannel> {

	private final IUser user;

	public class dm {

		public String recipient_id;

		public dm(String r) {
			recipient_id = r;
		}
	}

	public CreateDMChannel(IUser user) {
		super(user.getLoader(), Endpoints.currentUserChannels, Methods.POST, new RESTOptions(new JSONObject().put("recipient_id", SnowflakeUtil.toString(user))));
		this.user = user;
		this.autoExecute();
	}

	@Override
	public RestAction<IPrivateChannel> execute() {
		if (!executed.getAndSet(true)) {
			if (user.getPrivateChannel() != null) {
				future.complete(user.getPrivateChannel());
			} else {
				CompletableFuture<ChannelJSON> cf = this.sendRequest(ChannelJSON.class);
				cf.thenAcceptAsync(data -> {
					future.complete((IPrivateChannel) EntityBuilder.getChannelFactory().buildChannel(data, loader, null));
				});
				cf.exceptionally(ex -> {
					future.completeExceptionally(ex);
					return null;
				});
			}
		}
		return this;
	}

}
