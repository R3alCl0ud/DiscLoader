package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberNicknameUpdateEvent extends DLEvent {

	private IGuildMember member;
	private String nick, oldNick;

	public GuildMemberNicknameUpdateEvent(IGuildMember member, String nick, String oldNick) {
		super(member.getLoader());
		this.member = member;
		this.nick = nick;
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
		return nick;
	}

	/**
	 * @return the oldNick
	 */
	public String getOldNickname() {
		return oldNick;
	}

}
