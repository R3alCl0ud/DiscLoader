package io.discloader.discloader.network.rest.actions.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class DeleteMessage<T extends ITextChannel> extends RESTAction<IMessage> {
	
	private T channel;
	private IMessage message;
	
	public DeleteMessage(T channel, IMessage message) {
		super(channel.getLoader());
		this.channel = channel;
		this.message = message;
	}
	
	public CompletableFuture<IMessage> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.message(channel.getID(), message.getID()), Methods.DELETE, true));
	}
	
}
