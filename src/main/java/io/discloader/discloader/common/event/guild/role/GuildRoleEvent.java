package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.common.event.guild.GuildEvent;
import io.discloader.discloader.entity.guild.IRole;

public class GuildRoleEvent extends GuildEvent {

	private final IRole role;

	public GuildRoleEvent(IRole role) {
		super(role.getGuild());
		this.role = role;
	}

	/**
	 * @return the role
	 */
	public IRole getRole() {
		return role;
	}

}
