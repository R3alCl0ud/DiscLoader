package io.discloader.discloader.network.rest.actions;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.util.DLUtil.Endpoints;;

public class InviteAction extends RESTAction<IInvite> {

	private String code;
	private int method;

	public InviteAction(String code, int method) {
		super(DiscLoader.getDiscLoader());
		this.code = code;
		this.method = method;
	}

	public InviteAction(IInvite invite, int method) {
		this(invite.getCode(), method);
	}

	public CompletableFuture<IInvite> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.invite(code), method, true));
	}

	public void complete(String s, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex.getCause());
			return;
		}
	}
}
