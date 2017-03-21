package io.discloader.discloader.network.rest.actions;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class PinnedMessages extends RESTAction<HashMap<String, Message>> {
	private ITextChannel channel;

	public PinnedMessages(ITextChannel channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public CompletableFuture<HashMap<String, Message>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelPins(channel.getID()), Methods.GET, true));
	}

	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
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
	}
}
