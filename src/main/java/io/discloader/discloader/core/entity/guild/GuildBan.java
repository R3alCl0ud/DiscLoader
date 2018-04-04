package io.discloader.discloader.core.entity.guild;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuildBan;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.BanJSON;

public class GuildBan implements IGuildBan {

	private final String reason;
	private final IUser user;

	public GuildBan(BanJSON data) {
		this.reason = data.reason;
		this.user = EntityRegistry.addUser(data.user);
		this.user.setup(data.user);
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public IUser getUser() {
		return user;
	}
}
