/**
 * 
 */
package io.disc.DiscLoader.events;

import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildMember;

/**
 * @author Perry Berman
 *
 */
public class GuildMemberUpdateEvent {

	public final Guild guild;
	public final GuildMember member;
	public final GuildMember oldMember;
	/**
	 * 
	 */
	public GuildMemberUpdateEvent(GuildMember member, GuildMember oldMember, Guild guild) {
		this.guild = guild;
		this.member = member;
		this.oldMember = oldMember;
	}

}
