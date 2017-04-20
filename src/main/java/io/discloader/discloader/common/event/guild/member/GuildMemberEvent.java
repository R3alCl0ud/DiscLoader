package io.discloader.discloader.common.event.guild.member;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class GuildMemberEvent extends DLEvent {

	private IGuildMember member;

	public GuildMemberEvent(IGuildMember member) {
		super(member.getLoader());
		this.member = member;
	}

	public IGuildMember getMember() {
		return member;
	}

	public IGuild getGuild() {
		return member.getGuild();
	}

	public static class NicknameUpdateEvent extends GuildMemberEvent {

		private String oldNick;

		public NicknameUpdateEvent(IGuildMember member, String oldNick) {
			super(member);
			this.oldNick = oldNick;
		}

		public IGuild getGuild() {
			return getMember().getGuild();
		}

		public String getNickname() {
			return getMember().getNickname();
		}

		public String getOldNickname() {
			return oldNick;
		}
	}

	public static class VoiceJoinEvent extends GuildMemberEvent {

		public VoiceJoinEvent(IGuildMember member) {
			super(member);

		}

		public IGuildVoiceChannel getChannel() {
			return getMember().getVoiceChannel();
		}
	}

	public static class VoiceSwitchEvent extends GuildMemberEvent {

		private IGuildVoiceChannel oldChannel;

		public VoiceSwitchEvent(IGuildMember member, IGuildVoiceChannel oldChannel) {
			super(member);
			this.oldChannel = oldChannel;
		}

		public IGuildVoiceChannel getOldVoiceChannel() {
			return oldChannel;
		}

		public IGuildVoiceChannel getVoiceChannel() {
			return getMember().getVoiceChannel();
		}

	}

	public static class VoiceLeaveEvent extends GuildMemberEvent {

		private IGuildVoiceChannel channel;

		public VoiceLeaveEvent(IGuildMember member, IGuildVoiceChannel channel) {
			super(member);
			this.channel = channel;
		}

		public IGuildVoiceChannel getVoiceChannel() {
			return channel;
		}

	}
}
