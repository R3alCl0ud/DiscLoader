package io.discloader.discloader.network.rest.actions.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class DeleteReaction extends RESTAction<IMessage> {

	private String emoji;
	private IMessage message;

	public DeleteReaction(IMessage message, String emoji) {
		super(message.getLoader());
		this.message = message;
		this.emoji = emoji;
	}

	public CompletableFuture<IMessage> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.currentUserReaction(message.getChannel().getID(), message.getID(), emoji), Methods.DELETE, true));
	}

	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex.getCause());
			return;
		}
		future.complete(message);
	}

}
