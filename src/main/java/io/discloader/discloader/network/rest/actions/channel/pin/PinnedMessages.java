package io.discloader.discloader.network.rest.actions.channel.pin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class PinnedMessages<T extends ITextChannel> extends RESTAction<Map<Long, IMessage>> {

	private T channel;

	public PinnedMessages(T channel) {
		super(channel.getLoader());
		this.channel = channel;
	}

	public CompletableFuture<Map<Long, IMessage>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelPins(channel.getID()), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		} else {
			MessageJSON[] data = DLUtil.gson.fromJson(s, MessageJSON[].class);
			HashMap<Long, IMessage> messages = new HashMap<>();
			for (MessageJSON m : data) {
				IMessage message = new Message<T>(channel, m);
				channel.getMessages().put(message.getID(), message);
				messages.put(message.getID(), message);
			}
			future.complete(messages);
		}
	}
}
