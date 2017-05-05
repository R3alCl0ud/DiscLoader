package io.discloader.discloader.network.rest.actions;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class ModifyUser extends RESTAction<DLUser> {

	private DLUser user;
	private JSONObject payload;

	public ModifyUser(DLUser user, JSONObject data) {
		super(user.getLoader());
		this.user = user;
		payload = data;
	}

	public CompletableFuture<DLUser> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.currentUser, Methods.PATCH, true, payload));
	}

	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex.getCause());
			return;
		}
		// user.
		future.complete(user);
	}

}
