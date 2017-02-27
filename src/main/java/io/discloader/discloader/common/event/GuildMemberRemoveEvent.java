package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;

public class GuildMemberRemoveEvent extends DLEvent {

	public final GuildMember member;
	
	public final Guild guild;
	
	public GuildMemberRemoveEvent(GuildMember member) {
		super(member.loader);
		
		this.member = member;
		this.guild = member.guild;
	}

}
