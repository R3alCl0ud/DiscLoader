package io.discloader.discloader.network.rest.actions;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class RESTFetchMessages {
	public DiscLoader loader;
	public ITextChannel channel;
	public MessageFetchOptions options;

	public RESTFetchMessages(ITextChannel channel, MessageFetchOptions options) {
		loader = channel.getLoader();
		this.channel = channel;
		this.options = options;
	}

	public CompletableFuture<HashMap<String, Message>> execute() {
		CompletableFuture<HashMap<String, Message>> future = new CompletableFuture<>();
		loader.rest.makeRequest(Endpoints.messages(channel.getID()), Methods.GET, true, options)
				.whenCompleteAsync((s, ex) -> {
					if (ex != null) {
						future.completeExceptionally(ex);
					} else {
						MessageJSON[] data = DLUtil.gson.fromJson(s, MessageJSON[].class);
						HashMap<String, Message> messages = new HashMap<>();
						for (MessageJSON m : data) {
							Message message = new Message(channel, m);
							channel.getMessages().put(message.id, message);
							messages.put(message.id, message);
						}
						future.complete(messages);
					}
				});
		return future;
	}
}
