package io.discloader.discloader.common.event.guild.role;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IRole;

public class GuildRoleCreateEvent extends DLEvent {

	public final IRole role;
	
	public GuildRoleCreateEvent(IRole role2) {
		super(role2.getLoader());
		
		this.role = role2;
	}

}
