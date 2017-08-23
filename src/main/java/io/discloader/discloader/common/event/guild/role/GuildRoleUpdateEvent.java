package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.entity.guild.IRole;

public class GuildRoleUpdateEvent extends GuildRoleEvent {

	private final IRole oldRole;

	public GuildRoleUpdateEvent(IRole role, IRole oldRole) {
		super(role);

		this.oldRole = oldRole;
	}

	/**
	 * @return the oldRole
	 */
	public IRole getOldRole() {
		return oldRole;
	}

}
