package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;

/**
 * Fired when a role is added to a guild member.
 * 
 * @author Perry Berman
 */
public class GuildMemberRoleAddEvent extends GuildMemberUpdateEvent {

	private final IRole role;

	public GuildMemberRoleAddEvent(IGuildMember oldMember, IGuildMember member, IRole newRole) {
		super(member, oldMember);
		this.role = newRole;
	}

	/**
	 * Returns the role that was added to the member.
	 * 
	 * @return The Role that was added to the member.
	 */
	public IRole getRole() {
		return role;
	}

}
