package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.common.entity.guild.GuildMember;
import io.discloader.discloader.common.event.DLEvent;

public class GuildMemberRemoveEvent extends DLEvent {

	public final GuildMember member;
	
	public final Guild guild;
	
	public GuildMemberRemoveEvent(GuildMember member) {
		super(member.getLoader());
		
		this.member = member;
		this.guild = member.guild;
	}

}
