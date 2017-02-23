package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Role;

public class GuildRoleUpdateEvent extends DiscEvent {

	public final Role role;
	
	public final Role oldRole;
	
	public GuildRoleUpdateEvent(Role role, Role oldRole) {
		super(role.loader);
		
		this.role = role;
		this.oldRole = oldRole;
	}


}
