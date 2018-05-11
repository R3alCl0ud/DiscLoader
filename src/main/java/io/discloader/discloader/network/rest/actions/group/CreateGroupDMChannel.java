package io.discloader.discloader.network.rest.actions.group;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 *
 */
public class CreateGroupDMChannel extends RestAction<IGroupChannel> {
	
	public CreateGroupDMChannel(DiscLoader loader, RESTOptions options) {
		super(loader, Endpoints.currentUserChannels, Methods.POST, options);
	}
	
	@Override
	public RestAction<IGroupChannel> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<ChannelJSON> cf = sendRequest(ChannelJSON.class);
			cf.thenAcceptAsync((data) -> {
				future.complete((IGroupChannel) EntityRegistry.addChannel(data, loader));
			});
			cf.exceptionally(ex -> {
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}
	
}
