/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;

/**
 * @author Perry Berman
 *
 */
public class GuildBanAddEvent extends DLEvent {

	public final GuildMember member;
	
	public final Guild guild;
	
	public GuildBanAddEvent(GuildMember member) {
		super(member.loader);
		
		this.member = member;
		
		this.guild = this.member.guild;
	}

}
