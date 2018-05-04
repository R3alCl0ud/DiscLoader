package io.discloader.discloader.network.rest.actions;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

/**
 * @author Perry Berman
 */
public class FetchUserProfile {

	private final IUser user;

	public FetchUserProfile(IUser user) {
		this.user = user;
	}

	public CompletableFuture<IUserProfile> execute() {
		CompletableFuture<IUserProfile> future = new CompletableFuture<>();
		user.getLoader().rest.makeRequest(Endpoints.userProfile(user.getID()), Methods.GET, true).whenCompleteAsync((s, ex) -> {
			if (s != null)
				System.out.println(s);
		});
		return future;
	}
}
