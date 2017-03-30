package io.discloader.discloader.network.rest.actions.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class FetchMessages<T extends ITextChannel<T>> extends RESTAction<Map<String, IMessage<T>>> {

	public T channel;
	public MessageFetchOptions options;

	public FetchMessages(T channel, MessageFetchOptions options) {
		super(channel.getLoader());
		this.channel = channel;
		this.options = options;
	}

	public CompletableFuture<Map<String, IMessage<T>>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.messages(channel.getID()), Methods.GET, true, options));
	}

	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		} else {
			MessageJSON[] data = DLUtil.gson.fromJson(s, MessageJSON[].class);
			HashMap<String, IMessage<T>> messages = new HashMap<>();
			for (MessageJSON m : data) {
				IMessage<T> message = new Message<>(channel, m);
				channel.getMessages().put(message.getID(), message);
				messages.put(message.getID(), message);
			}
			future.complete(messages);
		}
	}
}
