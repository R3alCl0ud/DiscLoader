/**
 * 
 */
package io.discloader.discloader.events;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.objects.structures.Guild;
import io.discloader.discloader.objects.structures.GuildMember;

/**
 * The object passed to the {@literal "PresenceUpdate"} event
 * @see DiscLoader
 * @see Guild
 * @see GuildMember
 * @author Perry Berman
 * @since 0.0.1_Alpha
 */
public class GuildMemberUpdateEvent {

	/**
	 * The current instance of {@link DiscLoader loader}
	 */
	public final DiscLoader loader;
	
	/**
	 * The guild the member belongs to.
	 */
	public final Guild guild;
	
	/**
	 * The updated member object
	 */
	public final GuildMember member;
	
	/**
	 * A copy of the member object from before the member updated
	 */
	public final GuildMember oldMember;

	public GuildMemberUpdateEvent(GuildMember member, GuildMember oldMember, Guild guild) {
		this.guild = guild;
		this.loader = this.guild.loader;
		this.member = member;
		this.oldMember = oldMember;
	}

}
