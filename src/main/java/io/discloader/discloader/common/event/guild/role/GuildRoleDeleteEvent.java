package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.entity.guild.IRole;

public class GuildRoleDeleteEvent extends GuildRoleEvent {

	public GuildRoleDeleteEvent(IRole role) {
		super(role);
	}

}
