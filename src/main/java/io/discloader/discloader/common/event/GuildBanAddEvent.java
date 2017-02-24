/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;

/**
 * @author Perry Berman
 *
 */
public class GuildBanAddEvent extends DiscEvent {

	public final GuildMember member;
	
	public final Guild guild;
	
	/**
	 * 
	 * 
	 */
	public GuildBanAddEvent(GuildMember member) {
		super(member.loader);
		
		this.member = member;
		
		this.guild = this.member.guild;
	}

}
