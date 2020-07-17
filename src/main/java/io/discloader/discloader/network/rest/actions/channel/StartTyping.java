package io.discloader.discloader.network.rest.actions.channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class StartTyping extends RESTAction<Map<Long, IUser>> {
	private ITextChannel channel;
	
	public StartTyping(ITextChannel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}
	
	public CompletableFuture<Map<Long, IUser>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelTyping(channel.getID()), Methods.POST, true));
	}
	
	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		} else {
			channel.getTyping().put(loader.user.getID(), loader.user);
			future.complete(channel.getTyping());
		}
	}
	
}
