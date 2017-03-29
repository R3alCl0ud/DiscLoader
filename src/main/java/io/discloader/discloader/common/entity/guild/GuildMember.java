package io.discloader.discloader.common.entity.guild;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.entity.Permission;
import io.discloader.discloader.common.entity.Presence;
import io.discloader.discloader.common.entity.channel.VoiceChannel;
import io.discloader.discloader.common.entity.user.User;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.VoiceConnectionException;
import io.discloader.discloader.entity.Permissions;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.rest.actions.guild.ModifyMember;
import io.discloader.discloader.util.DLUtil;

/**
 * Represents a member in a {@link Guild}
 * 
 * @author Perry Berman
 * @see User
 * @see Guild
 */
public class GuildMember {

	/**
	 * The loader instance that cached the member.
	 */
	public final DiscLoader loader;

	/**
	 * The member's nickname, or null if the user has no nickname
	 */
	private String nick;

	/**
	 * The member's Snowflake ID.
	 * 
	 * @see User
	 */
	private final String id;

	/**
	 * The member's user object
	 */
	public final User user;

	/**
	 * The guild the member is in
	 */
	public final Guild guild;

	private String[] roleIDs;

	/**
	 * Whether or not the member's microphone is muted
	 */
	private boolean mute;

	/**
	 * Whether or not the member has muted their audio.
	 */
	private boolean deaf;

	/**
	 * Member's old presence. Has a value of {@code null} unless
	 */
	private Presence presence;

	/**
	 * A {@link Date} object representing when the member joined the
	 * {@link Guild}.
	 */
	public final Date joinedAt;

	public GuildMember(Guild guild, MemberJSON data) {
		loader = guild.getLoader();
		user = loader.addUser(data.user);
		id = user.getID();
		this.guild = guild;
		nick = data.nick != null ? data.nick : user.getUsername();
		joinedAt = DLUtil.parseISO8601(data.joined_at);
		roleIDs = data.roles != null ? data.roles : new String[] {};

		deaf = data.deaf;
		mute = deaf || data.mute;

	}

	public GuildMember(Guild guild, User user) {
		id = user.getID();

		this.user = user;

		this.guild = guild;

		loader = guild.getLoader();
		roleIDs = new String[] {};

		joinedAt = Date.from(Instant.now());
	}

	public GuildMember(Guild guild, User user, String[] roles, boolean deaf, boolean mute, String nick) {
		id = user.getID();
		this.user = user;
		this.guild = guild;
		loader = guild.getLoader();
		this.nick = nick != null ? nick : user.getUsername();
		roleIDs = roles != null ? roles : new String[] {};
		joinedAt = Date.from(Instant.now());
		this.deaf = deaf;
		this.mute = deaf || mute;
	}

	public GuildMember(GuildMember data) {
		id = data.id;
		loader = data.loader;
		user = data.user;
		guild = data.guild;
		nick = data.nick;
		roleIDs = data.roleIDs;
		joinedAt = data.joinedAt;
		deaf = data.deaf;
		mute = deaf || data.mute;
	}

	/**
	 * Bans the member from the {@link Guild} if the {@link DiscLoader loader}
	 * has sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 */
	public CompletableFuture<GuildMember> ban() {
		return guild.ban(this);
	}

	/**
	 * Server deafens a {@link GuildMember} if they are not already server
	 * deafened
	 * 
	 * @return A Future that completes with the deafed member if successful.
	 */
	public CompletableFuture<GuildMember> deafen() {
		ModifyMember future = new ModifyMember(this, nick, getRoles(), mute, true, getVoiceChannel());
		return future.execute();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GuildMember)) return false;
		GuildMember member = (GuildMember) obj;
		if (!guild.getID().equals(member.guild.getID())) return false;
		for (Role role : member.getRoles().values()) {
			if (!getRoles().containsKey(role.id)) return false;
		}

		return id.equals(member.id) && nick.equals(member.nick);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * Determines the member's highest role in the {@link #guild}'s role hiarchy
	 * 
	 * @return A Role object
	 */
	public Role getHighestRole() {
		Role highest = null;
		for (Role role : getRoles().values()) {
			if (highest == null || highest.getPosition() < role.getPosition()) {
				highest = role;
				continue;
			}
		}
		return highest;
	}

	public String getID() {
		return id;
	}

	/**
	 * @return the member's nickname if they have one, {@link #user}
	 *         {@link User#username .username} otherwise.
	 */
	public String getName() {
		return nick != null ? nick : user.getUsername();
	}

	/**
	 * @return the member's nickname if they have one, null otherwise.
	 */
	public String getNickname() {
		return nick;
	}

	/**
	 * Gets the member's presence from their {@link #guild}
	 * 
	 * @return The members current presence.
	 */
	public Presence getPresence() {
		if (presence == null) presence = guild.presences.get(id);
		return presence;
	}

	public Permission getPermissions() {
		int r = 0;
		for (Role role : getRoles().values()) {
			r |= role.getPermissions().asInteger();
		}
		return new Permission(this, r);
	}

	/**
	 * returns a HashMap of roles that the member belongs to.
	 * 
	 * @return A HashMap of the member's roles. indexed by {@link Role#id}
	 */
	public HashMap<String, Role> getRoles() {
		HashMap<String, Role> roles = new HashMap<String, Role>();
		for (String id : roleIDs) {
			roles.put(id, guild.roles.get(id));
		}
		return roles;
	}

	/**
	 * Gets the {@link VoiceChannel} that the member is connected to, if they're
	 * in a voice channel.
	 * 
	 * @return a {@link VoiceChannel} object if {@link #hasVoiceConnection()}
	 *         returns true, null otherwise.
	 */
	public VoiceChannel getVoiceChannel() {
		VoiceState vs = guild.getVoiceStates().get(id);
		if (vs != null) {
			return (VoiceChannel) vs.channel;
		}
		return null;
	}

	public VoiceState getVoiceState() {
		return guild.getVoiceStates().get(id);
	}

	/**
	 * Gives a member a new role
	 * 
	 * @param roles The roles to give to the member
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 * @throws PermissionsException thrown if a role with a higher position than
	 *             the current user's highest role is attempted to be given to
	 *             the member. Also thrown if the current user doesn't have the
	 *             MANAGE_ROLE permission.
	 */
	public CompletableFuture<GuildMember> giveRole(Role... roles) {
		if (!guild.isOwner() && !guild.getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_ROLES.getValue())) throw new PermissionsException("");
		for (Role role : roles)
			if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) throw new PermissionsException("");
		ArrayList<CompletableFuture<GuildMember>> futures = new ArrayList<>();
		CompletableFuture<GuildMember> future = new CompletableFuture<>();
		for (Role role : roles) {
			futures.add(loader.rest.giveRole(this, role));
		}
		CompletableFuture.allOf((CompletableFuture<?>[]) futures.toArray()).thenAcceptAsync(action -> {
			future.complete(this);
		});
		return future;
	}

	/**
	 * Determines if the member is connected to a voice channel in their
	 * {@link #guild}
	 * 
	 * @return {@code true} if the member has a {@link VoiceState},
	 *         {@code false} otherwise.
	 */
	public boolean hasVoiceConnection() {
		return getVoiceState() != null;
	}

	public boolean isDeaf() {
		if (!hasVoiceConnection()) return deaf;

		return getVoiceState().deaf || deaf;
	}

	public boolean isMuted() {
		if (!hasVoiceConnection()) return mute;

		return getVoiceState().mute || mute;
	}

	/**
	 * Kicks the member from the {@link Guild} if the {@link DiscLoader client}
	 * has sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 * @throws PermissionsException Thrown if the current user doesn't have the
	 *             {@link Permissions#KICK_MEMBERS} permission.
	 */
	public CompletableFuture<GuildMember> kick() {
		return guild.kickMember(this);
	}

	/**
	 * Move the {@link GuildMember member} to a different {@link VoiceChannel}
	 * if they are already connected to a {@link VoiceChannel}.
	 * 
	 * @param channel The {@link VoiceChannel} to move the member to.
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException thrown if the current user doesn't have the
	 *             {@link Permissions#MOVE_MEMBERS} permission.
	 * @throws VoiceConnectionException Thrown if the member is not in a voice
	 *             channel.
	 */
	public CompletableFuture<GuildMember> move(VoiceChannel channel) {
		if (!guild.isOwner() && !channel.permissionsFor(guild.getCurrentMember()).hasPermission(Permissions.MOVE_MEMBERS.getValue())) throw new PermissionsException("Insuccficient Permissions");
		if (getVoiceChannel() == null) throw new VoiceConnectionException();
		ModifyMember future = new ModifyMember(this, channel);
		return future.execute();
	}

	/**
	 * Server deafens a {@link GuildMember} if they are not already server
	 * deafened
	 * 
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 * @throws PermissionsException thrown if the current user doesn't have the
	 *             {@link Permissions#MUTE_MEMBERS} permission.
	 */
	public CompletableFuture<GuildMember> mute() {
		if (!guild.isOwner() && !guild.getCurrentMember().getPermissions().hasPermission(Permissions.MUTE_MEMBERS.getValue())) throw new PermissionsException("Insuccficient Permissions");
		ModifyMember future = new ModifyMember(this, nick, getRoles(), true, deaf, getVoiceChannel());
		return future.execute();
	}

	/**
	 * Sets the member's nickname if the {@link DiscLoader loader} has
	 * sufficient permissions. Requires the {@link Permissions#MANAGE_NICKNAMES}
	 * permission.
	 * 
	 * @param nick The member's new nickname
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 * @throws PermissionsException thrown if the current user doesn't have the
	 *             {@link Permissions#MANAGE_NICKNAMES} permission.
	 */
	public CompletableFuture<GuildMember> setNick(String nick) {
		if (!guild.isOwner() && !guild.getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_NICKNAMES.getValue())) throw new PermissionsException("Insuccficient Permissions");

		CompletableFuture<GuildMember> future = loader.rest.setNick(this, nick);
		future.thenAcceptAsync(action -> this.nick = nick);
		return future;
	}

	/**
	 * Takes a role away from a member
	 * 
	 * @param role The role to take away from the member
	 * @return A Future that completes with the member if successful.
	 * @throws PermissionsException thrown if a role with a higher position than
	 *             the current user's highest role is attempted to be given to
	 *             the member. Also thrown if the current user doesn't have the
	 *             MANAGE_ROLE permission.
	 */
	public CompletableFuture<GuildMember> takeRole(Role role) {
		if (!guild.isOwner() && !guild.getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_ROLES.getValue())) throw new PermissionsException("Insuccficient Permissions");
		if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) throw new PermissionsException("Cannot take away roles higher than your's");

		return loader.rest.takeRole(this, role);
	}

	/**
	 * Same as {@link User#toString()}
	 * 
	 * @return a string in mention format
	 */
	public String toMention() {
		return String.format("<@!%s>", id);
	}

	public CompletableFuture<GuildMember> unDeafen() {
		ModifyMember future = new ModifyMember(this, nick, getRoles(), mute, false, getVoiceChannel());
		return future.execute();
	}

	public CompletableFuture<GuildMember> unMute() {
		ModifyMember future = new ModifyMember(this, nick, getRoles(), false, deaf, getVoiceChannel());
		return future.execute();
	}

}
