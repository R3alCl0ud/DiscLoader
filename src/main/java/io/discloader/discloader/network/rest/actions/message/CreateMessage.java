package io.discloader.discloader.network.rest.actions.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class CreateMessage extends RestAction<IMessage> {

	private final ITextChannel channel;

	public CreateMessage(ITextChannel channel, SendableMessage data) {
		super(channel.getLoader(), Endpoints.messages(channel.getID()), Methods.POST, new RESTOptions(data));
		this.channel = channel;
		autoExecute();
	}

	@Override
	public RestAction<IMessage> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<MessageJSON> cf = sendRequest(MessageJSON.class);
			cf.thenAcceptAsync((data) -> {
				future.complete(new Message<>(channel, data));
			});
			cf.exceptionally(ex -> {
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}
}
