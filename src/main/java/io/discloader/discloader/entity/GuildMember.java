package io.discloader.discloader.entity;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.util.Constants;

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
	public boolean mute;
	/**
	 * Whether or not the member
	 */
	public boolean deaf;

	/**
	 * Member's old presence. Has a value of {@code null} unless
	 */
	public Presence frozenPresence;

	/**
	 * A {@link Date} object representing when the member joined the
	 * {@link Guild}.
	 */
	public final Date joinedAt;

	/**
	 * Default GuildMember constructor
	 * 
	 * @param guild
	 *            The {@link Guild} the member belongs to
	 * @param data
	 *            The member's data
	 */
	public GuildMember(Guild guild, MemberJSON data) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(data.user);
		this.id = this.user.id;
		this.guild = guild;
		this.nick = data.nick != null ? data.nick : this.user.username;
		this.joinedAt = Constants.parseISO8601(data.joined_at);
		this.roleIDs = data.roles;

		this.deaf = data.deaf;
		this.mute = this.deaf ? true : data.mute;

	}

	/**
	 * @param guild
	 * @param user
	 * @param roles
	 * @param deaf
	 * @param mute
	 * @param nick
	 */
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

	/**
	 * @param data
	 */
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

	public GuildMember(Guild guild, User user) {
		this.id = user.id;

		this.user = user;

		this.guild = guild;
		
		this.loader = guild.loader;
		
		this.joinedAt = null;
	}

	/**
	 * @see {@link User.toString()}
	 */
	public String toString() {
		return this.user.toString();
	}

	public Presence getPresence() {
		return this.guild.presences.get(this.user.id);
	}

	public HashMap<String, Role> getRoleList() {
		HashMap<String, Role> roles = new HashMap<String, Role>();
		for (String id : this.roleIDs) {
			roles.put(id, this.guild.roles.get(id));
		}
		return roles;
	}

	/**
	 * Sets the member's nickname if the {@link DiscLoader loader} has suficient
	 * permissions
	 * 
	 * @param nick
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successfull
	 */
	public CompletableFuture<GuildMember> setNick(String nick) {
		return this.loader.rest.setNick(this, nick);
	}

	/**
	 * Bans the member from the {@link Guild} if the {@link DiscLoader loader}
	 * has suficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successfull
	 */
	public CompletableFuture<GuildMember> ban() {
		return null;
	}

	/**
	 * Kicks the member from the {@link Guild} if the {@link DiscLoader loader}
	 * has suficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successfull
	 */
	public CompletableFuture<GuildMember> kick() {
		return null;
	}
}
