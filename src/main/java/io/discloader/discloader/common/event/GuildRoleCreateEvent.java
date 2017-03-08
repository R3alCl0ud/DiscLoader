package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.guild.Role;

public class GuildRoleCreateEvent extends DLEvent {

	public final Role role;
	
	public GuildRoleCreateEvent(Role role) {
		super(role.loader);
		
		this.role = role;
	}

}
