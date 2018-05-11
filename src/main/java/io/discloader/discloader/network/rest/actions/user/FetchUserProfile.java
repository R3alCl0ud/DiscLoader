package io.discloader.discloader.network.rest.actions.user;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.user.UserProfile;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.network.json.ProfileJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 */
public class FetchUserProfile extends RestAction<IUserProfile> {

	public FetchUserProfile(IUser user) {
		super(user.getLoader(), Endpoints.userProfile(user.getID()), Methods.GET, new RESTOptions());
		autoExecute();
	}

	@Override
	public RestAction<IUserProfile> execute() {
		if (!executed.getAndSet(true)) {
			CompletableFuture<ProfileJSON> cf = this.sendRequest(ProfileJSON.class);
			cf.thenAcceptAsync(data -> {
				future.complete(new UserProfile(loader, data));
			});
			cf.exceptionally(ex -> {
				future.completeExceptionally(ex);
				return null;
			});
		}
		return this;
	}

}
