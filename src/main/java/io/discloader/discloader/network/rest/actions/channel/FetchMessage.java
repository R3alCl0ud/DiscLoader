package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class FetchMessage<T extends ITextChannel<T>> extends RESTAction<IMessage<T>> {

	public T channel;
	public String messageID;

	public FetchMessage(T channel, String messageID) {
		super(channel.getLoader());
		this.channel = channel;
		this.messageID = messageID;
	}

	public CompletableFuture<IMessage<T>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.message(channel.getID(), messageID), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		MessageJSON data = DLUtil.gson.fromJson(s, MessageJSON.class);
		IMessage<T> message = new Message<>(channel, data);
		channel.getMessages().put(message.getID(), message);
		future.complete(message);
	}
}
