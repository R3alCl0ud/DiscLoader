/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.common.entity.guild.GuildMember;
import io.discloader.discloader.common.event.DLEvent;

/**
 * @author Perry Berman
 *
 */
public class GuildBanAddEvent extends DLEvent {

	public final GuildMember member;
	
	public final Guild guild;
	
	public GuildBanAddEvent(GuildMember member) {
		super(member.getLoader());
		
		this.member = member;
		
		this.guild = this.member.guild;
	}

}
