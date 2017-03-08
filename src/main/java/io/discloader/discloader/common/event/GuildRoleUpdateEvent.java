package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.guild.Role;

public class GuildRoleUpdateEvent extends DLEvent {

	public final Role role;
	
	public final Role oldRole;
	
	public GuildRoleUpdateEvent(Role role, Role oldRole) {
		super(role.loader);
		
		this.role = role;
		this.oldRole = oldRole;
	}


}
