package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.GuildMember;

import java.util.HashMap;

/**
 * Default Implementation of the {@link IEventListener}
 * 
 * @author Perry Berman
 * @see IEventListener
 */
public abstract class EventListenerAdapter implements IEventListener {

	@Override
	public void raw(String raw) {
	}

	@Override
	public void PreInit(DLPreInitEvent preInitEvent) {
	}

	@Override
	public void PhaseChange() {
	}

	@Override
	public void Ready(DiscLoader loader) {
	}

	@Override
	public void GuildCreate(GuildCreateEvent e) {
	}

	@Override
	public void GuildDelete(GuildDeleteEvent e) {
	}

	@Override
	public void GuildUpdate(GuildUpdateEvent e) {
	}

	@Override
	public void GuildMembersChunk(HashMap<String, GuildMember> members) {
	}

	@Override
	public void GuildMemberAdd(GuildMemberAddEvent e) {
	}

	@Override
	public void GuildMemberRemove(GuildMemberRemoveEvent e) {
	}

	@Override
	public void GuildMemberUpdate(GuildMemberUpdateEvent e) {
	}

	@Override
	public void GuildMemberAvailable(GuildMember member) {
	}

	@Override
	public void GuildBanAdd(GuildBanAddEvent e) {
	}

	@Override
	public void GuildBanRemove(GuildBanRemoveEvent e) {
	}

	@Override
	public void GuildRoleCreate(GuildRoleCreateEvent e) {
	}

	@Override
	public void GuildRoleDelete(GuildRoleDeleteEvent e) {
	}

	@Override
	public void GuildRoleUpdate(GuildRoleUpdateEvent e) {
	}

	@Override
	public void GuildEmojisUpdate() {
	}

	@Override
	public void ChannelCreate(ChannelCreateEvent e) {
	}

	@Override
	public void ChannelDelete(ChannelDeleteEvent e) {
	}

	@Override
	public void ChannelUpdate(ChannelUpdateEvent e) {
	}

	@Override
	public void ChannelPinsUpdate() {
	}

	@Override
	public void MessageCreate(MessageCreateEvent e) {
	}

	@Override
	public void MessageDelete(MessageDeleteEvent e) {
	}

	@Override
	public void MessageUpdate(MessageUpdateEvent e) {
	}

	@Override
	public void PrivateMessageCreate(MessageCreateEvent e) {
	}

	@Override
	public void PrivateMessageDelete(MessageDeleteEvent e) {
	}

	@Override
	public void PrivateMessageUpdate(MessageUpdateEvent e) {
	}

	@Override
	public void UserUpdate(UserUpdateEvent e) {
	}

	@Override
	public void PresenceUpdate() {
	}

}
