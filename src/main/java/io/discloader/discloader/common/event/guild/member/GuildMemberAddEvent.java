package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberAddEvent extends DLEvent {

	private IGuildMember member;

	public GuildMemberAddEvent(IGuildMember member2) {
		super(member2.getLoader());
		this.member = member2;
	}

	public IGuild getGuild() {
		return member.getGuild();
	}

	public IGuildMember getMember() {
		return member;
	}

}
