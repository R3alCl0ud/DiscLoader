package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Role;

public class GuildRoleUpdateEvent extends DLEvent {

	public final Role role;
	
	public final Role oldRole;
	
	public GuildRoleUpdateEvent(Role role, Role oldRole) {
		super(role.getLoader());
		
		this.role = role;
		this.oldRole = oldRole;
	}


}
