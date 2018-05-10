package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class CreateInvite extends RestAction<IInvite> {

	protected final IGuildChannel channel;

	public CreateInvite(IGuildChannel channel, JSONObject data) {
		super(channel.getLoader(), Endpoints.channelInvites(channel.getID()), Methods.POST, new RESTOptions(data));
		this.channel = channel;
		autoExecute();
	}

	@Override
	public RestAction<IInvite> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<InviteJSON> cf = sendRequest(InviteJSON.class);
			cf.thenAcceptAsync(data -> {
				future.complete(EntityBuilder.getInviteFactory().buildInvite(data));
			});
			cf.exceptionally(ex -> {
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}

}
