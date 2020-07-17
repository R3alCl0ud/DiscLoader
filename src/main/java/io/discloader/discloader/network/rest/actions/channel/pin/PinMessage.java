package io.discloader.discloader.network.rest.actions.channel.pin;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class PinMessage<T extends ITextChannel> extends RESTAction<IMessage> {

	private IMessage message;

	public PinMessage(IMessage toPin) {
		super(toPin.getLoader());
		message = toPin;
	}

	public CompletableFuture<IMessage> execute() {
		String endpoint = Endpoints.channelPinnedMessage(message.getChannel().getID(), message.getID());
		return super.execute(loader.rest.makeRequest(endpoint, Methods.PUT, true));
	}

	@Override
	public void complete(String d, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex.getCause());
			return;
		}
		future.complete(message);
	}
}
