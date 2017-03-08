package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;

public class GuildMemberRemoveEvent extends DLEvent {

	public final GuildMember member;
	
	public final Guild guild;
	
	public GuildMemberRemoveEvent(GuildMember member) {
		super(member.loader);
		
		this.member = member;
		this.guild = member.guild;
	}

}
