package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IRole;

public class GuildRoleUpdateEvent extends DLEvent {
	
	public final IRole role;
	
	public final IRole oldRole;
	
	public GuildRoleUpdateEvent(IRole role2, IRole oldRole2) {
		super(role2.getLoader());
		
		this.role = role2;
		this.oldRole = oldRole2;
	}
	
}
