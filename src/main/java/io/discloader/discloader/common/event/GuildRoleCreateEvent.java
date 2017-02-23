package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Role;

public class GuildRoleCreateEvent extends DiscEvent {

	public final Role role;
	
	public GuildRoleCreateEvent(Role role) {
		super(role.loader);
		
		this.role = role;
	}

}
