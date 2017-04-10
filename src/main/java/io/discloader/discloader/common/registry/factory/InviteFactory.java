package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IInvite;
import io.discloader.discloader.entity.invite.Invite;
import io.discloader.discloader.network.json.InviteJSON;

public class InviteFactory {

	public IInvite buildInvite(InviteJSON data) {
		return new Invite(data, DiscLoader.getDiscLoader());
	}
}
