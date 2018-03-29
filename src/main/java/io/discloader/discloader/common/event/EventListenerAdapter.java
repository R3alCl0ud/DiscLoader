package io.discloader.discloader.common.event;

import java.util.Map;

import io.discloader.discloader.common.event.channel.ChannelCreateEvent;
import io.discloader.discloader.common.event.channel.ChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.ChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelCreateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.GuildChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.TypingStartEvent;
import io.discloader.discloader.common.event.guild.GuildAvailableEvent;
import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.common.event.guild.GuildBanRemoveEvent;
import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.common.event.guild.GuildDeleteEvent;
import io.discloader.discloader.common.event.guild.GuildEvent;
import io.discloader.discloader.common.event.guild.GuildSyncEvent;
import io.discloader.discloader.common.event.guild.GuildUnavailableEvent;
import io.discloader.discloader.common.event.guild.GuildUpdateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceJoinEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceLeaveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceSwitchEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRoleAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRoleRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleDeleteEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.common.event.message.GroupMessageCreateEvent;
import io.discloader.discloader.common.event.message.GroupMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GroupMessageUpdateEvent;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GuildMessageUpdateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageReactionAddEvent;
import io.discloader.discloader.common.event.message.MessageReactionRemoveEvent;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageDeleteEvent;
import io.discloader.discloader.common.event.message.PrivateMessageUpdateEvent;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.IGuildEmoji;

/**
 * Default Implementation of the {@link IEventListener}
 * 
 * @author Perry Berman
 * @see IEventListener
 */
public abstract class EventListenerAdapter implements IEventListener {

	@Override
	public void ChannelCreate(ChannelCreateEvent e) {}

	@Override
	public void ChannelDelete(ChannelDeleteEvent e) {}

	@Override
	public void ChannelPinsUpdate() {}

	@Override
	public void ChannelUpdate(ChannelUpdateEvent e) {}

	@Override
	public void Disconnected(DisconnectEvent e) {}

	@Override
	public void GroupMessageCreate(GroupMessageCreateEvent e) {}

	@Override
	public void GroupMessageDelete(GroupMessageDeleteEvent e) {}

	@Override
	public void GroupMessageUpdate(GroupMessageUpdateEvent e) {}

	@Override
	public void GuildAvailable(GuildAvailableEvent e) {}

	@Override
	public void GuildBanAdd(GuildBanAddEvent e) {}

	@Override
	public void GuildBanRemove(GuildBanRemoveEvent e) {}

	@Override
	public void GuildChannelCreate(GuildChannelCreateEvent event) {}

	@Override
	public void GuildChannelDelete(GuildChannelDeleteEvent event) {}

	@Override
	public void GuildChannelUpdate(GuildChannelUpdateEvent event) {}

	@Override
	public void GuildCreate(GuildCreateEvent e) {}

	@Override
	public void GuildDelete(GuildDeleteEvent e) {}

	@Override
	public void GuildEmojiCreate(GuildEmojiCreateEvent event) {}

	@Override
	public void GuildEmojiDelete(GuildEmojiDeleteEvent event) {}

	@Override
	public void GuildEmojisUpdate(Map<Long, IGuildEmoji> emojis) {}

	@Override
	public void GuildEmojiUpdate(GuildEmojiUpdateEvent event) {}

	@Override
	public void GuildEvent(GuildEvent e) {}

	@Override
	public void GuildMemberAdd(GuildMemberAddEvent e) {}

	@Override
	public void GuildMemberAvailable(GuildMember member) {}

	@Override
	public void GuildMemberEvent(GuildMemberEvent event) {}

	@Override
	public void GuildMemberNicknameUpdated(GuildMemberEvent.NicknameUpdateEvent event) {}

	@Override
	public void GuildMemberRemove(GuildMemberRemoveEvent e) {}

	@Override
	public void GuildMemberRoleAdd(GuildMemberRoleAddEvent e) {}

	@Override
	public void GuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {}

	@Override
	public void GuildMembersChunk(GuildMembersChunkEvent event) {}

	@Override
	public void GuildMemberUpdate(GuildMemberUpdateEvent e) {}

	@Override
	public void GuildMemberVoiceJoin(VoiceJoinEvent event) {}

	@Override
	public void GuildMemberVoiceLeave(VoiceLeaveEvent event) {}

	@Override
	public void GuildMemberVoiceSwitch(VoiceSwitchEvent event) {}

	@Override
	public void GuildMessageCreate(GuildMessageCreateEvent event) {}

	@Override
	public void GuildMessageDelete(GuildMessageDeleteEvent event) {}

	@Override
	public void GuildMessageUpdate(GuildMessageUpdateEvent event) {}

	@Override
	public void GuildRoleCreate(GuildRoleCreateEvent e) {}

	@Override
	public void GuildRoleDelete(GuildRoleDeleteEvent e) {}

	@Override
	public void GuildRoleEvent(io.discloader.discloader.common.event.guild.role.GuildRoleEvent e) {}

	@Override
	public void GuildRoleUpdate(GuildRoleUpdateEvent e) {}

	@Override
	public void GuildSync(GuildSyncEvent event) {}

	@Override
	public void GuildUnavailable(GuildUnavailableEvent e) {}

	@Override
	public void GuildUpdate(GuildUpdateEvent e) {}

	@Override
	public void MessageCreate(MessageCreateEvent e) {}

	@Override
	public void MessageDelete(MessageDeleteEvent e) {}

	@Override
	public void MessageReactionAdd(MessageReactionAddEvent e) {}

	@Override
	public void MessageReactionRemove(MessageReactionRemoveEvent e) {}

	@Override
	public void MessageUpdate(MessageUpdateEvent e) {}

	@Override
	public void PhaseChange() {}

	@Override
	public void PreInit(DLPreInitEvent preInitEvent) {}

	@Override
	public void PresenceUpdate() {}

	@Override
	public void PrivateMessageCreate(PrivateMessageCreateEvent e) {}

	@Override
	public void PrivateMessageDelete(PrivateMessageDeleteEvent e) {}

	@Override
	public void PrivateMessageUpdate(PrivateMessageUpdateEvent e) {}

	@Override
	public void RawPacket(RawEvent event) {}

	@Override
	public void Ready(ReadyEvent event) {}

	public void Reconnect(ReconnectEvent e) {

	}

	@Override
	public void SendingPacket(Object obj) {}

	@Override
	public void TypingStart(TypingStartEvent event) {}

	@Override
	public void UserUpdate(UserUpdateEvent e) {}

	@Override
	public void VoiceStateUpdate(VoiceStateUpdateEvent e) {}

}
