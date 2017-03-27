package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channels.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class FetchMessage extends RESTAction<Message> {
	public ITextChannel channel;
	public String messageID;

	public FetchMessage(ITextChannel channel, String messageID) {
		super(channel.getLoader());
		this.channel = channel;
		this.messageID = messageID;
	}

	public CompletableFuture<Message> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.message(channel.getID(), messageID), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		MessageJSON data = DLUtil.gson.fromJson(s, MessageJSON.class);
		Message message = new Message(channel, data);
		channel.getMessages().put(message.id, message);
		future.complete(message);
	}
}
