package io.discloader.discloader.network.rest.actions.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class FetchInvites extends RestAction<List<IInvite>> {

	public FetchInvites(IGuildChannel channel) {
		super(channel.getLoader(), Endpoints.channelInvites(channel.getID()), Methods.GET, new RESTOptions());
		autoExecute();
	}

	public RestAction<List<IInvite>> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<InviteJSON[]> cf = sendRequest(InviteJSON[].class);
			cf.thenAcceptAsync(data -> {
				List<IInvite> invites = new ArrayList<>();
				for (InviteJSON invite : data) {
					invites.add(EntityBuilder.getInviteFactory().buildInvite(invite, loader));
				}
				future.complete(invites);
			});
			cf.exceptionally(ex -> {
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}
}
