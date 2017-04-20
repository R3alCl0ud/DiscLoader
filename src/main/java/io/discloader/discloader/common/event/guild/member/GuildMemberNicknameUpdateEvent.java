package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberNicknameUpdateEvent extends DLEvent {

	private IGuildMember member;
	private String oldNick;

	public GuildMemberNicknameUpdateEvent(IGuildMember member, String oldNick) {
		super(member.getLoader());
		this.member = member;
		this.oldNick = oldNick;
	}

	/**
	 * @return the member
	 */
	public IGuildMember getMember() {
		return member;
	}

	public IGuild getGuild() {
		return member.getGuild();
	}

	/**
	 * @return the nick
	 */
	public String getNickname() {
		return member.getNickname();
	}

	/**
	 * @return the oldNick
	 */
	public String getOldNickname() {
		return oldNick;
	}

}
