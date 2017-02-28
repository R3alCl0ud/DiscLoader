package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;

import java.util.HashMap;

/**
 * Interface for DiscLoader events
 * @author Perry Berman
 * @since 0.0.3
 */
public interface IEventListener {

	/**
	 * Emitted whenever a new packet is received from the gateway
	 * 
	 * @param raw
	 */
	void raw(String raw);

	/**
	 * During the PreInit phase of startup, after all mods have been
	 * discovered, The {@link ModRegistry} executes this event on the mod the
	 * registry is attempting to load. All commands <u>must</u> be registered on
	 * this event being called in your mod.
	 * 
	 * @param preInitEvent
	 */
	void PreInit(DLPreInitEvent preInitEvent);

	/**
	 * Gets emitted during startup, when the current startup phase changes
	 * 
	 */
	void PhaseChange();

	/**
	 * Emitted when all guilds are available, and all members in non-large
	 * {@link Guild guilds} are cached.
	 * 
	 * @param loader The current instance of {@link DiscLoader}
	 */
	void Ready(DiscLoader loader);

	/**
	 * Executed when the client joins a new {@link Guild}
	 * @param e A GuildCreateEvent object
	 */
	void GuildCreate(GuildCreateEvent e);

	/**
	 * Executed when the client leaves a {@link Guild}
	 * @param e A {@link GuildDeleteEvent}
	 */
	void GuildDelete(GuildDeleteEvent e);

	/**
	 * @param e
	 */
	void GuildUpdate(GuildUpdateEvent e);

	/**
	 * @param members
	 */
	void GuildMembersChunk(HashMap<String, GuildMember> members);


	/**
	 * @param event
	 */
	void GuildMemberAdd(GuildMemberAddEvent event); 

	/**
	 * @param e
	 */
	void GuildMemberRemove(GuildMemberRemoveEvent e);

	/**
	 * @param e
	 */
	void GuildMemberUpdate(GuildMemberUpdateEvent e);

	/**
	 * @param member
	 */
	void GuildMemberAvailable(GuildMember member);

	/**
	 * @param e
	 */
	void GuildBanAdd(GuildBanAddEvent e);

	/**
	 * @param event
	 */
	void GuildBanRemove(GuildBanRemoveEvent event);

	/**
	 * Executed when a {@link Role} is created
	 * @param event A GuildRoleCreateEvent object
	 */
	void GuildRoleCreate(GuildRoleCreateEvent event);

	/**
	 * @param e
	 */
	void GuildRoleDelete(GuildRoleDeleteEvent e);

	/**
	 * @param e
	 */
	void GuildRoleUpdate(GuildRoleUpdateEvent e);

	void GuildEmojisUpdate();

	void ChannelCreate(ChannelCreateEvent e);

	void ChannelDelete(ChannelDeleteEvent e);

	void ChannelUpdate(ChannelUpdateEvent e);

	void ChannelPinsUpdate();

	void MessageCreate(MessageCreateEvent e);

	void MessageDelete(MessageDeleteEvent e);

	void MessageUpdate(MessageUpdateEvent e);

	void PrivateMessageCreate(MessageCreateEvent e);

	void PrivateMessageDelete(MessageDeleteEvent e);

	void PrivateMessageUpdate(MessageUpdateEvent e);

	void UserUpdate(UserUpdateEvent e);

	void PresenceUpdate();
}
