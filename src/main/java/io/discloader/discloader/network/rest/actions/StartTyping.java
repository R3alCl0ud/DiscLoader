package io.discloader.discloader.network.rest.actions;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channels.ITextChannel;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class StartTyping extends RESTAction<HashMap<String, User>> {
	private ITextChannel channel;

	public StartTyping(ITextChannel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public CompletableFuture<HashMap<String, User>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelTyping(channel.getID()), Methods.POST, true));
	}

	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		} else {
			channel.getTyping().put(loader.user.id, loader.user);
			future.complete(channel.getTyping());
		}
	}

}
