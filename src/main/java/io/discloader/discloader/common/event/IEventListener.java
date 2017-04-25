package io.discloader.discloader.common.event;

import java.util.Map;

import io.discloader.discloader.common.event.channel.ChannelCreateEvent;
import io.discloader.discloader.common.event.channel.ChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.ChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelCreateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.GuildChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.TypingStartEvent;
import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.common.event.guild.GuildBanRemoveEvent;
import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.common.event.guild.GuildDeleteEvent;
import io.discloader.discloader.common.event.guild.GuildSyncEvent;
import io.discloader.discloader.common.event.guild.GuildUpdateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.NicknameUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceJoinEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceLeaveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceSwitchEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleDeleteEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.common.event.message.GroupMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GuildMessageUpdateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageDeleteEvent;
import io.discloader.discloader.common.event.message.PrivateMessageUpdateEvent;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * Interface for DiscLoader events
 * <H1>How To Use</H1>
 * 
 * <pre>
 * DiscLoader loader = new DiscLoader();
 * 
 * loader.addListener(new EventListenerAdapter() {
 * 
 * 	&#64;Override
 * 	public void Ready(DiscLoader loader) {
 * 		// do something
 * 	}
 * 
 * });
 * </pre>
 * 
 * @author Perry Berman
 * @since 0.0.3
 */
public interface IEventListener {

	void ChannelCreate(ChannelCreateEvent e);

	void ChannelDelete(ChannelDeleteEvent e);

	void ChannelPinsUpdate();

	void ChannelUpdate(ChannelUpdateEvent e);

	void GroupMessageCreate(GroupMessageCreateEvent e);

	/**
	 * Executed when a {@link User} is banned from a guild
	 * 
	 * @param e The GuildBanAddEvent object to be passed to the handler
	 */
	void GuildBanAdd(GuildBanAddEvent e);

	/**
	 * Executed when a {@link User} is unbanned from a guild
	 * 
	 * @param event The GuildBanRemoveEvent object to be passed to the handler
	 */
	void GuildBanRemove(GuildBanRemoveEvent event);

	void GuildChannelCreate(GuildChannelCreateEvent event);

	void GuildChannelDelete(GuildChannelDeleteEvent event);

	void GuildChannelUpdate(GuildChannelUpdateEvent event);

	/**
	 * Executed when the client joins a new {@link Guild}
	 * 
	 * @param e A GuildCreateEvent object
	 */
	void GuildCreate(GuildCreateEvent e);

	/**
	 * Executed when the client leaves a {@link Guild}
	 * 
	 * @param e A {@link GuildDeleteEvent}
	 */
	void GuildDelete(GuildDeleteEvent e);

	void GuildEmojiCreate(GuildEmojiCreateEvent event);

	void GuildEmojiDelete(GuildEmojiDeleteEvent event);

	void GuildEmojisUpdate(Map<Long, IGuildEmoji> map);

	void GuildEmojiUpdate(GuildEmojiUpdateEvent event);

	/**
	 * Executed when a {@link GuildMember} joins a guild
	 * 
	 * @param event The GuildMemberAddEvent object to be passed to the handler
	 */
	void GuildMemberAdd(GuildMemberAddEvent event);

	/**
	 * Executed when a GuildMemberAvailable event is sent to the client
	 * 
	 * @param member The GuildMember that became available
	 */
	void GuildMemberAvailable(GuildMember member);

	void GuildMemberEvent(GuildMemberEvent event);

	void GuildMemberNicknameUpdated(NicknameUpdateEvent event);

	/**
	 * Executed when a {@link GuildMember} leaves, or is kicked from a guild
	 * 
	 * @param e The GuildMemberRemoveEvent object to be passed to the handler
	 */
	void GuildMemberRemove(GuildMemberRemoveEvent e);

	void GuildMemberRoleAdd();

	void GuildMemberRoleRemove();

	/**
	 * Executed when a GuildMembersChunk event is sent to the client
	 * 
	 * @param event A {@link GuildMembersChunkEvent} object.
	 */
	void GuildMembersChunk(GuildMembersChunkEvent event);

	/**
	 * Executed when a GuildMemberUpdate event is sent to the client
	 * 
	 * @param e A GuildMemberUpdateEvent object
	 */
	void GuildMemberUpdate(GuildMemberUpdateEvent e);

	void GuildMemberVoiceJoin(VoiceJoinEvent event);

	void GuildMemberVoiceLeave(VoiceLeaveEvent event);

	void GuildMemberVoiceSwitch(VoiceSwitchEvent event);

	void GuildMessageCreate(GuildMessageCreateEvent event);

	void GuildMessageDelete(GuildMessageDeleteEvent event);

	void GuildMessageUpdate(GuildMessageUpdateEvent event);

	/**
	 * Executed when a {@link Role} is created
	 * 
	 * @param event A GuildRoleCreateEvent object
	 */
	void GuildRoleCreate(GuildRoleCreateEvent event);

	/**
	 * Executed when a {@link Role} is deleted
	 * 
	 * @param e A GuildRoleDeleteEvent object
	 */
	void GuildRoleDelete(GuildRoleDeleteEvent e);

	/**
	 * Executed when a {@link Role} is updated
	 * 
	 * @param e A GuildRoleUpdateEvent object
	 */
	void GuildRoleUpdate(GuildRoleUpdateEvent e);

	void GuildSync(GuildSyncEvent event);

	/**
	 * Executed when a {@link Guild} is updated
	 * 
	 * @param e A GuildUpdateEvent object
	 */
	void GuildUpdate(GuildUpdateEvent e);
	
	void MessageCreate(MessageCreateEvent e);

	void MessageDelete(MessageDeleteEvent e);

	void MessageUpdate(MessageUpdateEvent e);

	/**
	 * Gets emitted during startup, when the current startup phase changes
	 */
	void PhaseChange();

	/**
	 * During the PreInit phase of startup, after all mods have been discovered,
	 * The {@link ModRegistry} executes this event on the mod the
	 * registry is attempting to load. All commands <u>must</u> be registered on
	 * this event being called in your mod.
	 * 
	 * @param preInitEvent A DLPreInitEvent Object
	 */
	void PreInit(DLPreInitEvent preInitEvent);

	void PresenceUpdate();

	void PrivateMessageCreate(PrivateMessageCreateEvent event);

	void PrivateMessageDelete(PrivateMessageDeleteEvent event);

	void PrivateMessageUpdate(PrivateMessageUpdateEvent event);

	/**
	 * Emitted whenever a new packet is received from the gateway. Is not
	 * executed when receiving packets from {@link VoiceConnection
	 * VoiceConnections}
	 * 
	 * @param event the raw data from the gateway/REST api.
	 */
	void RawPacket(RawEvent event);

	/**
	 * Emitted when all guilds are available, and all members in non-large
	 * {@link Guild guilds} are cached.
	 * 
	 * @param event A {@link ReadyEvent} object
	 */
	void Ready(ReadyEvent event);

	/**
	 * Emitted when a user starts typing in an {@link ITextChannel}
	 * 
	 * @param event A {@link TypingStartEvent} object
	 */
	void TypingStart(TypingStartEvent event);

	void UserUpdate(UserUpdateEvent e);

	void VoiceStateUpdate(VoiceStateUpdateEvent e);
}
