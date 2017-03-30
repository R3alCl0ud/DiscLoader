package io.discloader.discloader.network.rest.actions.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class CreateMessage<T extends ITextChannel> extends RESTAction<IMessage<T>> {

	private T channel;
	private SendableMessage message;

	public CreateMessage(T channel, SendableMessage data) {
		super(channel.getLoader());
		this.channel = channel;
		message = data;
	}

	public CompletableFuture<IMessage<T>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.messages(channel.getID()), Methods.POST, true, message));
	}
}
