package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberRemoveEvent extends DLEvent {
	
	private final IGuildMember member;
	
	private final IGuild guild;
	
	public GuildMemberRemoveEvent(IGuildMember member) {
		super(member.getLoader());
		
		this.member = member;
		this.guild = member.getGuild();
	}
	
	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return this.guild;
	}
	
	/**
	 * @return the member
	 */
	public IGuildMember getMember() {
		return this.member;
	}
	
}
