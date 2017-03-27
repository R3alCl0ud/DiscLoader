package io.discloader.discloader.network.rest.actions.channel.pin;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class UnpinMessage extends RESTAction<Message> {

	private Message message;

	public UnpinMessage(Message toUnpin) {
		super(toUnpin.loader);
		message = toUnpin;
	}

	public CompletableFuture<Message> execute() {
		String endpoint = Endpoints.channelPinnedMessage(message.channel.getID(), message.id);
		return super.execute(loader.rest.makeRequest(endpoint, Methods.DELETE, true));
	}

	public void complete(String d, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(message);
	}

}
