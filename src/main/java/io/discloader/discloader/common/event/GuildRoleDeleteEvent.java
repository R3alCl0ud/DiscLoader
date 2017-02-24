package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Role;

public class GuildRoleDeleteEvent extends DLEvent {

	public final Role role;
	
	public GuildRoleDeleteEvent(Role role) {
		super(role.loader);
		
		this.role = role;
	}


}
