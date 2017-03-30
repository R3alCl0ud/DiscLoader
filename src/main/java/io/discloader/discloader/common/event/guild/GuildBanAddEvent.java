/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

/**
 * @author Perry Berman
 *
 */
public class GuildBanAddEvent extends DLEvent {

	public final IGuildMember member;
	
	public final IGuild guild;
	
	public GuildBanAddEvent(IGuildMember member) {
		super(member.getLoader());
		
		this.member = member;
		
		this.guild = member.getGuild();
	}

}
