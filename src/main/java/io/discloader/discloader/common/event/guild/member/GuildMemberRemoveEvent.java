package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberRemoveEvent extends DLEvent {

	private final IGuildMember member;

	public GuildMemberRemoveEvent(IGuildMember member) {
		super(member.getLoader());
		this.member = member;
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return member.getGuild();
	}

	/**
	 * @return the member
	 */
	public IGuildMember getMember() {
		return this.member;
	}

}
