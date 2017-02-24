package io.discloader.discloader.common.event;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.User;

public interface IEventAdapter {

	/**
	 * Emitted whenever a new packet is recieved from the gateway
	 * 
	 * @param raw  
	 */
	void raw(String raw);

	/**
	 * Durring the PreInit phase of startup, after all mods have been
	 * discovered, The {@link ModRegistry} executes this event on the mod the
	 * registry is attempting to load. All commands <u>must</u> be registered on
	 * this event being called in your mod.
	 * 
	 * @param preInitEvent 
	 */
	void PreInit(DLPreInitEvent preInitEvent);
	
	/**
	 * 
	 */
	void PhaseChange();

	/**
	 * Emitted when all guilds are available, and all members in non-large
	 * {@link Guild guilds} are cached.
	 * 
	 * @param loader
	 *            The current instance of {@link DiscLoader}
	 */
	void Ready(DiscLoader loader);

	/**
	 * @param e
	 */
	void GuildCreate(GuildCreateEvent e);

	/**
	 * @param e
	 */
	void GuildDelete(GuildDeleteEvent e);

	/**
	 * @param e
	 */
	void GuildUpdate(GuildUpdateEvent e);

	/**
	 * 
	 */
	void GuildMembersChunk(HashMap<String, GuildMember> members);

	/**
	 * 
	 */
	void GuildMemberAdd(GuildMember member);

	/**
	 * 
	 */
	void GuildMemberRemove(GuildMember member);

	/**
	 * @param e
	 */
	void GuildMemberUpdate(GuildMemberUpdateEvent e);

	void GuildMemberAvailable(GuildMember member);

	void GuildBanAdd(GuildBanAddEvent e);

	void GuildBanRemove(User user);

	void GuildRoleCreate(GuildRoleCreateEvent e);

	void GuildRoleDelete(GuildRoleDeleteEvent e);

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
