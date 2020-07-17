package io.discloader.discloader.network.rest.actions.channel;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 */
public class BulkDelete<T extends ITextChannel> extends RESTAction<Map<Long, IMessage>> {

	public T channel;
	public Map<Long, IMessage> messages;

	public BulkDelete(T channel, Map<Long, IMessage> messages) {
		super(channel.getLoader());
		this.channel = channel;
		this.messages = messages;
	}

	public CompletableFuture<Map<Long, IMessage>> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.bulkDelete(channel.getID()), Methods.GET, true));
	}

	@Override
	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}
		future.complete(messages);
	}

}
