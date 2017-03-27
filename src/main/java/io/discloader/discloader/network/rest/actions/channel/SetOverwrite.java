package io.discloader.discloader.network.rest.actions.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.channels.impl.GuildChannel;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class SetOverwrite extends RESTAction<Overwrite> {

	private final Overwrite overwrite;
	private GuildChannel channel;

	public SetOverwrite(GuildChannel channel, Overwrite overwrite) {
		super(channel.getLoader());
		this.channel = channel;
		this.overwrite = overwrite;
	}

	public CompletableFuture<Overwrite> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.channelOverwrite(channel.getID(), overwrite.id), Methods.PUT, true, gson.toJson(overwrite)));
	}

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
	public Overwrite getOverwrite() {
		return overwrite;
	}

}
