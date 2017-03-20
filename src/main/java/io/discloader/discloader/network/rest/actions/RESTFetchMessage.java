package io.discloader.discloader.network.rest.actions;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class RESTFetchMessage {
	public DiscLoader loader;
	public ITextChannel channel;
	public String messageID;

	public RESTFetchMessage(ITextChannel channel, String messageID) {
		loader = channel.getLoader();
		this.channel = channel;
		this.messageID = messageID;
	}

	public CompletableFuture<Message> execute() {
		CompletableFuture<Message> future = new CompletableFuture<>();
		loader.rest.makeRequest(Endpoints.message(channel.getID(), messageID), Methods.GET, true)
				.whenCompleteAsync((s, ex) -> {
					if (ex != null) {
						future.completeExceptionally(ex);
					} else {
						MessageJSON data = DLUtil.gson.fromJson(s, MessageJSON.class);
						Message message = new Message(channel, data);
						channel.getMessages().put(message.id, message);
						future.complete(message);
					}
				});
		return future;
	}
}
