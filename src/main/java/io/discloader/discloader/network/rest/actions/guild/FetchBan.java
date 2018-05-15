package io.discloader.discloader.network.rest.actions.guild;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.exceptions.DiscordException;
import io.discloader.discloader.core.entity.guild.GuildBan;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildBan;
import io.discloader.discloader.network.json.BanJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class FetchBan extends RestAction<IGuildBan> {

	public FetchBan(IGuild guild, long userID) {
		super(guild.getLoader(), Endpoints.guildBanMember(guild.getID(), userID), Methods.GET, new RESTOptions());
		autoExecute();
	}

	@Override
	public RestAction<IGuildBan> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<BanJSON> cf = this.sendRequest(BanJSON.class);
			cf.thenAcceptAsync(data -> {
				future.complete(new GuildBan(data));
			});
			cf.exceptionally(ex -> {
				if (ex instanceof DiscordException) {
					DiscordException dex = (DiscordException) ex;
					if (dex.getErrorCode() == 10026) {
						future.complete(null);
						return null;
					}
				}
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}

}
