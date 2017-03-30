package io.discloader.discloader.core.entity.guild;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.VoiceConnectionException;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.Permissions;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.user.IUser;
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
public class GuildMember implements IGuildMember {
	
	/**
	 * The member's nickname, or null if the user has no nickname
	 */
	private String nick;
	
	/**
	 * The member's user object
	 */
	public final IUser user;
	
	/**
	 * The guild the member is in
	 */
	public final IGuild guild;
	
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
	 * A {@link Date} object representing when the member joined the {@link Guild}.
	 */
	public final Date joinedAt;
	
	public GuildMember(IGuild guild, MemberJSON data) {
		user = guild.getLoader().addUser(data.user);
		this.guild = guild;
		nick = data.nick != null ? data.nick : user.getUsername();
		joinedAt = DLUtil.parseISO8601(data.joined_at);
		roleIDs = data.roles != null ? data.roles : new String[] {};
		
		deaf = data.deaf;
		mute = deaf || data.mute;
		
	}
	
	public GuildMember(IGuild guild, IUser user) {
		this.user = user;
		
		this.guild = guild;
		
		roleIDs = new String[] {};
		
		joinedAt = Date.from(Instant.now());
	}
	
	public GuildMember(Guild guild, IUser user2, String[] roles, boolean deaf, boolean mute, String nick) {
		this.user = user2;
		this.guild = guild;
		this.nick = nick != null ? nick : user2.getUsername();
		roleIDs = roles != null ? roles : new String[] {};
		joinedAt = Date.from(Instant.now());
		this.deaf = deaf;
		this.mute = deaf || mute;
	}
	
	public GuildMember(IGuildMember member) {
		user = member.getUser();
		guild = member.getGuild();
		nick = member.getNickname();
		roleIDs = member.getRoles().keySet().toArray(new String[] { });
		joinedAt = Date.from(Instant.now());
		deaf = member.isDeaf();
		mute = deaf || member.isMuted();
	}
	
	/**
	 * Same as {@link User#toString()}
	 * 
	 * @return a string in mention format
	 */
	@Override
	public String asMention() {
		return String.format("<@!%s>", getID());
	}
	
	/**
	 * Bans the member from the {@link Guild} if the {@link DiscLoader loader} has sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if successful
	 */
	@Override
	public CompletableFuture<IGuildMember> ban() {
		return guild.ban(this);
	}
	
	/**
	 * Server deafens a {@link GuildMember} if they are not already server deafened
	 * 
	 * @return A Future that completes with the deafed member if successful.
	 */
	@Override
	public CompletableFuture<IGuildMember> deafen() {
		return new ModifyMember(this, nick, getRoles(), mute, true, getVoiceChannel()).execute();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GuildMember))
			return false;
		GuildMember member = (GuildMember) obj;
		if (!guild.getID().equals(member.guild.getID()))
			return false;
		for (IRole role : member.getRoles().values()) {
			if (!getRoles().containsKey(role.getID()))
				return false;
		}
		
		return getID().equals(member.getID()) && nick.equals(member.nick);
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuildMember#getGuild()
	 */
	@Override
	public IGuild getGuild() {
		return guild;
	}
	
	/**
	 * Determines the member's highest role in the {@link #guild}'s role hiarchy
	 * 
	 * @return A Role object
	 */
	@Override
	public IRole getHighestRole() {
		IRole highest = null;
		for (IRole role : getRoles().values()) {
			if (highest == null || highest.getPosition() < role.getPosition()) {
				highest = role;
				continue;
			}
		}
		return highest;
	}
	
	@Override
	public String getID() {
		return user.getID();
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuildMember#getLoader()
	 */
	@Override
	public DiscLoader getLoader() {
		return guild.getLoader();
	}
	
	/**
	 * @return the member's nickname if they have one, {@link #user} {@link User#username .username} otherwise.
	 */
	public String getName() {
		return nick != null ? nick : user.getUsername();
	}
	
	/**
	 * @return the member's nickname if they have one, null otherwise.
	 */
	@Override
	public String getNickname() {
		return nick;
	}
	
	@Override
	public IPermission getPermissions() {
		int r = 0;
		for (IRole role : getRoles().values()) {
			r |= role.getPermissions().toInt();
		}
		return new Permission(this, r);
	}
	
	/**
	 * Gets the member's presence from their {@link #guild}
	 * 
	 * @return The members current presence.
	 */
	@Override
	public IPresence getPresence() {
		return guild.getPresences().get(getID());
	}
	
	/**
	 * returns a HashMap of roles that the member belongs to.
	 * 
	 * @return A HashMap of the member's roles. indexed by {@link Role#id}
	 */
	@Override
	public HashMap<String, IRole> getRoles() {
		HashMap<String, IRole> roles = new HashMap<>();
		for (String id : roleIDs) {
			roles.put(id, guild.getRoles().get(id));
		}
		return roles;
	}
	
	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuildMember#getUser()
	 */
	@Override
	public IUser getUser() {
		return user;
	}
	
	/**
	 * Gets the {@link VoiceChannel} that the member is connected to, if they're in a voice channel.
	 * 
	 * @return a {@link VoiceChannel} object if {@link #hasVoiceConnection()} returns true, null otherwise.
	 */
	@Override
	public IVoiceChannel getVoiceChannel() {
		VoiceState vs = guild.getVoiceStates().get(getID());
		
		return vs == null ? null : vs.channel;
	}
	
	@Override
	public VoiceState getVoiceState() {
		return guild.getVoiceStates().get(getID());
	}
	
	/**
	 * Gives a member a new role
	 * 
	 * @param roles The roles to give to the member
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException thrown if a role with a higher position than the current user's highest role is attempted to be given to
	 *             the member. Also thrown if the current user doesn't have the MANAGE_ROLE permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> giveRole(IRole... roles) {
		if (!guild.hasPermission(Permissions.MANAGE_ROLES)) {
			throw new PermissionsException("");
		}
			
		for (IRole role : roles) {
			if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) {
				throw new PermissionsException("");
			}
		}
	
		ArrayList<CompletableFuture<GuildMember>> futures = new ArrayList<>();
		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		for (IRole role : roles) {
			futures.add(getLoader().rest.giveRole(this, role));
		}
		CompletableFuture.allOf((CompletableFuture<?>[]) futures.toArray()).thenAcceptAsync(action -> {
			future.complete(this);
		});
		return future;
	}
	
	@Override
	public int hashCode() {
		return getID().hashCode();
	}
	
	/**
	 * Determines if the member is connected to a voice channel in their {@link #guild}
	 * 
	 * @return {@code true} if the member has a {@link VoiceState}, {@code false} otherwise.
	 */
	public boolean hasVoiceConnection() {
		return getVoiceState() != null;
	}
	
	@Override
	public boolean isDeaf() {
	    return hasVoiceConnection() ? (getVoiceState().deaf || deaf) : deaf;
	}
	
	@Override
	public boolean isMuted() {
	    return hasVoiceConnection() ? (getVoiceState().mute || mute) : mute;
	}
	
	/**
	 * Kicks the member from the {@link Guild} if the {@link DiscLoader client} has sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException Thrown if the current user doesn't have the {@link Permissions#KICK_MEMBERS} permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> kick() {
		return guild.kickMember(this);
	}
	
	/**
	 * Move the {@link GuildMember member} to a different {@link VoiceChannel} if they are already connected to a {@link VoiceChannel}.
	 * 
	 * @param channel The {@link VoiceChannel} to move the member to.
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException thrown if the current user doesn't have the {@link Permissions#MOVE_MEMBERS} permission.
	 * @throws VoiceConnectionException Thrown if the member is not in a voice channel.
	 */
	@Override
	public CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel) {
		if (!guild.isOwner() && !channel.permissionsFor(guild.getCurrentMember()).hasPermission(Permissions.MOVE_MEMBERS))
			throw new PermissionsException("Insufficient Permissions");
		if (getVoiceChannel() == null)
			throw new VoiceConnectionException();
		
		return new ModifyMember(this, channel).execute();
	}
	
	/**
	 * Server deafens a {@link GuildMember} if they are not already server deafened
	 * 
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException thrown if the current user doesn't have the {@link Permissions#MUTE_MEMBERS} permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> mute() {
        if (!guild.hasPermission(Permissions.MUTE_MEMBERS)) {
            throw new PermissionsException("Insufficient Permissions");
        }
	
        return new ModifyMember(this, nick, getRoles(), true, deaf, getVoiceChannel()).execute();
    }
	
	/**
	 * Sets the member's nickname if the {@link DiscLoader loader} has sufficient permissions. Requires the
	 * {@link Permissions#MANAGE_NICKNAMES} permission.
	 * 
	 * @param nick The member's new nickname
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException thrown if the current user doesn't have the {@link Permissions#MANAGE_NICKNAMES} permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> setNick(String nick) {
        if (!guild.hasPermission(Permissions.MANAGE_NICKNAMES)) {
            throw new PermissionsException("Insuccficient Permissions");
        }

        CompletableFuture<IGuildMember> future = getLoader().rest.setNick(this, nick);
        future.thenAcceptAsync(action -> this.nick = nick);
        return future;
    }
	
	/**
	 * Takes a role away from a member
	 * 
	 * @param role The role to take away from the member
	 * @return A Future that completes with the member if successful.
	 * @throws PermissionsException thrown if a role with a higher position than the current user's highest role is attempted to be given to
	 *             the member. Also thrown if the current user doesn't have the MANAGE_ROLE permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> takeRole(IRole role) {
		if (!guild.hasPermission(Permissions.MANAGE_ROLES)) {
			throw new PermissionsException("Insuccficient Permissions");
		}
	
		if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition())
			throw new PermissionsException("Cannot take away roles higher than your's");
		
		return getLoader().rest.takeRole(this, role);
	}
	
	@Override
	public CompletableFuture<IGuildMember> unDeafen() {
		return new ModifyMember(this, nick, getRoles(), mute, false, getVoiceChannel()).execute();
	}
	
	@Override
	public CompletableFuture<IGuildMember> unMute() {
		return new ModifyMember(this, nick, getRoles(), false, deaf, getVoiceChannel()).execute();
	}
	
}
