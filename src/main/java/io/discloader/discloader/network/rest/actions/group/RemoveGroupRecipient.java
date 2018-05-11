package io.discloader.discloader.network.rest.actions.group;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 *
 */
public class RemoveGroupRecipient extends RestAction<IGroupChannel> {
	
	/**
	 * @param loader
	 * @param endpoint
	 * @param method
	 * @param options
	 */
	public RemoveGroupRecipient(IGroupChannel channel, IUser user) {
		super(channel.getLoader(), Endpoints.channelRecipient(channel.getID(), user.getID()), Methods.DELETE, new RESTOptions());
	}
	
	@Override
	public RestAction<IGroupChannel> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<ChannelJSON> cf = sendRequest(ChannelJSON.class);
			cf.thenAcceptAsync((data) -> {
				future.complete((IGroupChannel) EntityBuilder.getChannelFactory().buildChannel(data, loader, null));
			});
			cf.exceptionally(ex -> {
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}
	
}
