package io.discloader.discloader.entity.guild;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Permission;
import io.discloader.discloader.entity.Presence;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.rest.actions.ModifyMember;
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
	public String nick;

	/**
	 * The member's Snowflake ID.
	 * 
	 * @see User
	 */
	public final String id;

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
	 * Whether or not the member's mic is muted
	 */
	private boolean mute;
	/**
	 * Whether or not the member
	 */
	private boolean deaf;

	/**
	 * Member's old presence. Has a value of {@code null} unless
	 */
	public Presence frozenPresence;

	/**
	 * A {@link Date} object representing when the member joined the
	 * {@link Guild}.
	 */
	public final Date joinedAt;

	public GuildMember(Guild guild, MemberJSON data) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(data.user);
		this.id = this.user.id;
		this.guild = guild;
		this.nick = data.nick != null ? data.nick : this.user.username;
		this.joinedAt = DLUtil.parseISO8601(data.joined_at);
		this.roleIDs = data.roles;

		this.deaf = data.deaf;
		this.mute = this.deaf ? true : data.mute;

	}

	public GuildMember(Guild guild, User user) {
		this.id = user.id;

		this.user = user;

		this.guild = guild;

		this.loader = guild.loader;

		this.joinedAt = null;
	}

	public GuildMember(Guild guild, User user, String[] roles, boolean deaf, boolean mute, String nick) {
		this.id = user.id;
		this.user = user;
		this.guild = guild;
		this.loader = guild.loader;
		this.nick = nick != null ? nick : this.user.username;
		this.roleIDs = roles;
		this.joinedAt = Date.from(Instant.now());
		this.deaf = deaf;
		this.mute = this.deaf == true ? true : mute;
	}

	public GuildMember(GuildMember data) {
		this.id = data.id;
		this.loader = data.loader;
		this.user = data.user;
		this.guild = data.guild;
		this.nick = data.nick;
		this.roleIDs = data.roleIDs;
		this.joinedAt = data.joinedAt;
		this.deaf = data.deaf;
		this.mute = this.deaf ? true : data.mute;
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
		return this.guild.ban(this);
	}

	public CompletableFuture<GuildMember> deafen() {
		ModifyMember future = new ModifyMember(this, nick, getRoles(), mute, true, getVoiceChannel());
		return future.execute();
	}

	public Role getHighestRole() {
		Role highest = null;
		for (Role role : getRoles().values()) {
			if (highest == null || highest.getPosition() < role.getPosition())
				highest = role;
		}
		return highest;
	}

	/**
	 * @return The members current presence.
	 */
	public Presence getPresence() {
		return this.guild.presences.get(this.user.id);
	}

	/**
	 * @return A HashMap of the member's roles. indexed by {@link Role#id}
	 */
	public HashMap<String, Role> getRoles() {
		HashMap<String, Role> roles = new HashMap<String, Role>();
		for (String id : this.roleIDs) {
			roles.put(id, this.guild.roles.get(id));
		}
		return roles;
	}

	public VoiceChannel getVoiceChannel() {
		VoiceState vs = this.guild.getRawStates().get(id);
		if (vs != null) {
			return (VoiceChannel) vs.channel;
		}
		return null;
	}

	public VoiceState getVoiceState() {
		return guild.getRawStates().get(id);
	}

	/**
	 * Gives a member a new role
	 * 
	 * @param roles The roles to give to the member
	 * @return A Future that completes with the member if successful.
	 */
	public CompletableFuture<GuildMember> giveRole(Role... roles) {
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

	public boolean isDeaf() {
		return getVoiceState().deaf;
	}

	public boolean isMute() {
		return getVoiceState().mute;
	}

	/**
	 * Kicks the member from the {@link Guild} if the {@link DiscLoader client}
	 * has sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 */
	public CompletableFuture<GuildMember> kick() {
		return this.guild.kickMember(this);
	}

	public CompletableFuture<GuildMember> move(VoiceChannel channel) {
		ModifyMember future = new ModifyMember(this, channel);
		return future.execute();
	}

	public CompletableFuture<GuildMember> mute() {
		ModifyMember future = new ModifyMember(this, nick, getRoles(), true, deaf, getVoiceChannel());
		return future.execute();
	}

	/**
	 * Sets the member's nickname if the {@link DiscLoader loader} has
	 * sufficient permissions
	 * 
	 * @param nick The member's new nickname
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 */
	public CompletableFuture<GuildMember> setNick(String nick) {
		return this.loader.rest.setNick(this, nick);
	}

	/**
	 * Takes a role away from a member
	 * 
	 * @param role The role to take away from the member
	 * @return A Future that completes with the member if successful.
	 */
	public CompletableFuture<GuildMember> takeRole(Role role) {
		return this.loader.rest.takeRole(this, role);
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
