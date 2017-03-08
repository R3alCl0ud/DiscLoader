package io.discloader.discloader.common.event;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.entity.guild.Emoji;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.Role;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * Interface for DiscLoader events
 * <H1>How To Use</H1>
 * 
 * <pre>
 * DiscLoader.addListener(new IEventListener() {
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

	void GuildEmojisUpdate(HashMap<String, Emoji> emojis);

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

	/**
	 * Executed when a {@link GuildMember} leaves, or is kicked from a guild
	 * 
	 * @param e The GuildMemberRemoveEvent object to be passed to the handler
	 */
	void GuildMemberRemove(GuildMemberRemoveEvent e);

	/**
	 * Executed when a GuildMembersChunk event is sent to the client
	 * 
	 * @param members A HashMap of members
	 */
	void GuildMembersChunk(HashMap<String, GuildMember> members);

	/**
	 * Executed when a GuildMemberUpdate event is sent to the client
	 * 
	 * @param e A GuildMemberUpdateEvent object
	 */
	void GuildMemberUpdate(GuildMemberUpdateEvent e);

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
	 * 
	 */
	void PhaseChange();

	/**
	 * During the PreInit phase of startup, after all mods have been discovered,
	 * The {@link ModRegistry} executes this event on the mod the registry is
	 * attempting to load. All commands <u>must</u> be registered on this event
	 * being called in your mod.
	 * 
	 * @param preInitEvent A DLPreInitEvent Object
	 */
	void PreInit(DLPreInitEvent preInitEvent);

	void PresenceUpdate();

	void PrivateMessageCreate(MessageCreateEvent e);

	void PrivateMessageDelete(MessageDeleteEvent e);

	void PrivateMessageUpdate(MessageUpdateEvent e);

	/**
	 * Emitted whenever a new packet is received from the gateway. Is not
	 * executed when receiving packets from {@link VoiceConnection
	 * VoiceConnections}
	 * 
	 * @param raw the raw text from each websocket connection
	 */
	void raw(String raw);

	/**
	 * Emitted when all guilds are available, and all members in non-large
	 * {@link Guild guilds} are cached.
	 * 
	 * @param loader The current instance of {@link DiscLoader}
	 */
	void Ready(DiscLoader loader);

	void UserUpdate(UserUpdateEvent e);
}
