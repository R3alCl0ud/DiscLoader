package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class SetOverwrite extends RESTAction<IOverwrite> {

	private final IOverwrite overwrite;
	private IGuildChannel channel;

	public SetOverwrite(IGuildChannel channel, IOverwrite overwrite) {
		super(channel.getLoader());
		this.channel = channel;
		this.overwrite = overwrite;
	}

	public CompletableFuture<IOverwrite> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelOverwrite(channel.getID(), overwrite.getID()), Methods.PUT, true, gson.toJson(overwrite)));
	}

	@Override
	public void complete(String r, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		future.complete(overwrite);
	}

	/**
	 * @return the overwrite
	 */
	public IOverwrite getOverwrite() {
		return overwrite;
	}

}
