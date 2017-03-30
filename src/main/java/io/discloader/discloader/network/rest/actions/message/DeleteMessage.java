package io.discloader.discloader.network.rest.actions.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class DeleteMessage<T extends ITextChannel> extends RESTAction<IMessage<T>> {

	private T channel;
	private IMessage<T> message;

	public DeleteMessage(T channel, IMessage<T> message) {
		super(channel.getLoader());
		this.channel = channel;
		this.message = message;
	}

	public CompletableFuture<IMessage<T>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.message(channel.getID(), message.getID()), Methods.DELETE, true));
	}

}
