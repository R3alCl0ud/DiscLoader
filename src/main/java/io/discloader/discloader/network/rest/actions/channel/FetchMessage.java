package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class FetchMessage<T extends ITextChannel> extends RESTAction<IMessage> {

	public T channel;
	public long messageID;

	public FetchMessage(T channel, long messageID) {
		super(channel.getLoader());
		this.channel = channel;
		this.messageID = messageID;
	}

	public CompletableFuture<IMessage> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.message(channel.getID(), messageID), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		MessageJSON data = DLUtil.gson.fromJson(s, MessageJSON.class);
		IMessage message = new Message<>(channel, data);
		channel.getMessages().put(message.getID(), message);
		future.complete(message);
	}
}
