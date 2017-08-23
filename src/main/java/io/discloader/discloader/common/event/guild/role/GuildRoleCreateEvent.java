package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.entity.guild.IRole;

public class GuildRoleCreateEvent extends GuildRoleEvent {

	public GuildRoleCreateEvent(IRole role) {
		super(role);
	}

}
