package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.guild.Role;

public class GuildRoleDeleteEvent extends DLEvent {

	public final Role role;
	
	public GuildRoleDeleteEvent(Role role) {
		super(role.getLoader());
		
		this.role = role;
	}


}
