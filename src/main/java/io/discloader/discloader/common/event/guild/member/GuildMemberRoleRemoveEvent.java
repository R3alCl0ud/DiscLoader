package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;

/**
 * Fired when a role is removed from a guild member.
 * 
 * @author Perry Berman
 */
public class GuildMemberRoleRemoveEvent extends GuildMemberUpdateEvent {

	private final IRole role;

	public GuildMemberRoleRemoveEvent(IGuildMember oldMember, IGuildMember member, IRole newRole) {
		super(member, oldMember);
		this.role = newRole;
	}

	/**
	 * Returns the role that was removed from the member.
	 * 
	 * @return The Role that was removed from the member.
	 */
	public IRole getRole() {
		return role;
	}

}
