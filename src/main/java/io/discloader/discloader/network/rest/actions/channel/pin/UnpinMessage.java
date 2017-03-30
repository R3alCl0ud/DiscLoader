package io.discloader.discloader.network.rest.actions.channel.pin;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class UnpinMessage<T extends ITextChannel> extends RESTAction<IMessage> {
	
	private IMessage message;
	
	public UnpinMessage(IMessage toUnpin) {
		super(toUnpin.getLoader());
		message = toUnpin;
	}
	
	public CompletableFuture<IMessage> execute() {
		String endpoint = Endpoints.channelPinnedMessage(message.getChannel().getID(), message.getID());
		return super.execute(loader.rest.makeRequest(endpoint, Methods.DELETE, true));
	}
	
	@Override
	public void complete(String d, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex.getCause());
			return;
		}
		future.complete(message);
	}
	
}
