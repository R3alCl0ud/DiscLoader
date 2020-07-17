package io.discloader.discloader.network.rest.actions.channel.close;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class CloseChannel<T extends IChannel> extends RESTAction<T> {

	private T channel;

	public CloseChannel(T channel) {
		super(channel.getLoader());
	}

	public CompletableFuture<T> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channel(channel.getID()), Methods.DELETE, true));
	}

	@Override
	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		future.complete(getChannel());
	}

	/**
	 * @return the channel
	 */
	public T getChannel() {
		return channel;
	}

}
