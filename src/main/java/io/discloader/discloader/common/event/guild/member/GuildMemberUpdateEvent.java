/**
 * 
 */
package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

/**
 * The object passed to the {@literal "PresenceUpdate"} event
 * 
 * @see DiscLoader
 * @see Guild
 * @see GuildMember
 * @author Perry Berman
 * @since 0.0.1_Alpha
 */
public class GuildMemberUpdateEvent extends DLEvent {

	/**
	 * The guild the member belongs to.
	 */
	private final IGuild guild;

	/**
	 * The updated member object
	 */
	private final IGuildMember member;

	/**
	 * A copy of the member object from before the member updated
	 */
	private final IGuildMember oldMember;

	public GuildMemberUpdateEvent(IGuildMember member, IGuildMember oldMember) {
		super(member.getLoader());
		this.guild = member.getGuild();
		this.member = member;
		this.oldMember = oldMember;
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return guild;
	}

	/**
	 * @return the member
	 */
	public IGuildMember getMember() {
		return member;
	}

	/**
	 * @return the oldMember
	 */
	public IGuildMember getOldMember() {
		return oldMember;
	}

}
