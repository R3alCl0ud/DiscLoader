package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberAvailableEvent extends DLEvent {

	private IGuildMember member;

	public GuildMemberAvailableEvent(IGuildMember member) {
		super(member.getLoader());
		this.member = member;
	}

	public IGuild getGuild() {
		return member.getGuild();
	}

	public IGuildMember getMember() {
		return member;
	}

}
