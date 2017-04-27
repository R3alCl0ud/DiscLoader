package io.discloader.discloader.network.rest.actions.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class FetchInvites extends RESTAction<List<IInvite>> {

	private IGuildChannel channel;

	public FetchInvites(IGuildChannel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public CompletableFuture<List<IInvite>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelInvites(channel.getID()), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		List<IInvite> invites = new ArrayList<>();
		InviteJSON[] inviteJSONs = gson.fromJson(s, InviteJSON[].class);
		for (InviteJSON invite : inviteJSONs) {
			invites.add(EntityBuilder.getInviteFactory().buildInvite(invite));
		}
		future.complete(invites);
	}

}
