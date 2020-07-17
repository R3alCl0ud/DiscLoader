package io.discloader.discloader.network.rest.actions.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class CreateReaction extends RESTAction<Void> {

	private String emoji;
	private IMessage message;

	public CreateReaction(IMessage message, String emoji) {
		super(message.getLoader());
		this.message = message;
		this.emoji = emoji;
	}

	public CompletableFuture<Void> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.currentUserReaction(message.getChannel().getID(), message.getID(), emoji), Methods.PUT, true));
	}

	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(null);
	}

}
