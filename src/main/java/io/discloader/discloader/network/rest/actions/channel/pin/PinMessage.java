package io.discloader.discloader.network.rest.actions.channel.pin;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class PinMessage extends RESTAction<Message> {
	private Message message;

	public PinMessage(Message toPin) {
		super(toPin.loader);
		message = toPin;
	}

	public CompletableFuture<Message> execute() {
		String endpoint = Endpoints.channelPinnedMessage(message.channel.getID(), message.id);
		return super.execute(loader.rest.makeRequest(endpoint, Methods.PUT, true));
	}

	public void complete(String d, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(message);
	}
}
