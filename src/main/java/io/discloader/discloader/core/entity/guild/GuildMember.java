package io.discloader.discloader.core.entity.guild;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.NicknameUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.VoiceConnectionException;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildBan;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.presence.IPresence;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.guild.ModifyMember;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

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
	public final OffsetDateTime joinedAt;

	public GuildMember(IGuild guild, IUser user) {
		this.user = user;

		this.guild = guild;

		roleIDs = new String[] {};

		joinedAt = user.createdAt();
	}

	public GuildMember(IGuild guild, IUser user, String[] roles, boolean deaf, boolean mute, String nick) {
		this.user = user;
		this.guild = guild;
		this.nick = nick != null ? nick : user.getUsername();
		roleIDs = roles != null ? roles : new String[0];
		joinedAt = OffsetDateTime.now();
		this.deaf = deaf;
		this.mute = deaf || mute;
	}

	public GuildMember(IGuild guild, MemberJSON data) {
		user = EntityRegistry.addUser(data.user);
		this.guild = guild;
		nick = data.nick;
		joinedAt = data.joined_at == null ? user.createdAt() : OffsetDateTime.parse(data.joined_at);
		this.roleIDs = new String[data.roles.length];
		for (int i = 0; i < data.roles.length && i < this.roleIDs.length; i++) {
			this.roleIDs[i] = new String(data.roles[i]);
		}
		if (this.roleIDs == null) {
			this.roleIDs = new String[0];
		}
		deaf = data.deaf;
		mute = deaf || data.mute;

	}

	public GuildMember(IGuildMember member) {
		user = member.getUser();
		guild = member.getGuild();
		nick = member.getNickname();
		if (member instanceof GuildMember) {
			roleIDs = Arrays.copyOf(((GuildMember) member).roleIDs, ((GuildMember) member).roleIDs.length);
		} else {
			roleIDs = new String[member.getRoles().size()];
			for (int i = 0; i < member.getRoles().size(); i++) {
				roleIDs[i] = SnowflakeUtil.asString(member.getRoles().get(i));
			}
		}
		joinedAt = member.getJoinTime();
		deaf = member.isDeafened();
		mute = deaf || member.isMuted();
	}

	/**
	 * Bans the member from the {@link Guild} if the {@link DiscLoader loader} has
	 * sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if successful
	 */
	@Override
	public CompletableFuture<IGuildMember> ban() {
		return guild.ban(this);
	}

	@Override
	public CompletableFuture<IGuildMember> ban(String reason) {
		return guild.ban(this, reason);
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
	public CompletableFuture<IGuildMember> deafen(String reason) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<IGuildMember>();
		JSONObject payload = new JSONObject().put("deaf", true);
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		cf.thenAcceptAsync(v -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GuildMember))
			return false;
		GuildMember member = (GuildMember) obj;
		if (guild.getID() != member.guild.getID())
			return false;
		for (IRole role : member.getRoles()) {
			if (!getRoles().contains(role))
				return false;
		}

		return getID() == member.getID() && nick.equals(member.nick);
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public IRole getHighestRole() {
		if (getRoles().isEmpty())
			return null;
		return getRoles().get(getRoles().size() - 1);
	}

	@Override
	public long getID() {
		return user.getID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.discloader.discloader.entity.guild.IGuildMember#getJoinTime()
	 */
	@Override
	public OffsetDateTime getJoinTime() {
		return joinedAt;
	}

	@Override
	public DiscLoader getLoader() {
		return guild.getLoader();
	}

	/**
	 * @return the member's nickname if they have one, {@link #user}
	 *         {@link User#username .username} otherwise.
	 */
	@Override
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
		long r = 0;
		for (IRole role : getRoles()) {
			r |= role.getPermissions().toLong();
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

	@Override
	public List<IRole> getRoles() {
		List<IRole> roles = new ArrayList<>();
		for (String id : roleIDs) {
			roles.add(guild.getRoleByID(id));
		}
		roles.sort((a, b) -> {
			if (a.getPosition() < b.getPosition())
				return -1;
			if (a.getPosition() > b.getPosition())
				return 1;
			return 0;
		});
		return roles;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	/**
	 * Gets the {@link VoiceChannel} that the member is connected to, if they're in
	 * a voice channel.
	 * 
	 * @return a {@link VoiceChannel} object if {@link #hasVoiceConnection()}
	 *         returns true, null otherwise.
	 */
	@Override
	public IGuildVoiceChannel getVoiceChannel() {
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
	 * @param roles
	 *            The roles to give to the member
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException
	 *             thrown if a role with a higher position than the current user's
	 *             highest role is attempted to be given to the member. Also thrown
	 *             if the current user doesn't have the MANAGE_ROLE permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> giveRole(IRole... roles) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		if (!guild.hasPermission(Permissions.MANAGE_ROLES)) {
			future.completeExceptionally(new PermissionsException("Insufficient Permissions"));
			return future;
		}

		for (IRole role : roles) {
			if (role == null)
				continue;
			if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) {
				future.completeExceptionally(new PermissionsException("Can not assign higher role"));
				return future;
			}
		}

		List<IRole> rls = mergeRoles(roles);
		String[] ids = new String[rls.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = SnowflakeUtil.asString(rls.get(i));
		}

		JSONObject payload = new JSONObject().put("roles", ids);
		System.out.println(payload);
		CompletableFuture<Void> vcf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(payload), Void.class);
		vcf.thenAcceptAsync(v -> {
			getLoader().addEventListener(new EventListenerAdapter() {
				public void GuildMemberUpdate(GuildMemberUpdateEvent e) {
					future.complete(e.getMember());
					getLoader().removeEventListener(this);
				}
			});
		});
		vcf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});

		return future;
	}

	@Override
	public CompletableFuture<IGuildMember> giveRole(String reason, IRole... roles) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		if (!guild.hasPermission(Permissions.MANAGE_ROLES)) {
			future.completeExceptionally(new PermissionsException("Insufficient Permissions"));
			return future;
		}

		for (IRole role : roles) {
			if (role == null)
				continue;
			if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) {
				future.completeExceptionally(new PermissionsException("Can not assign higher role"));
				return future;
			}
		}

		List<IRole> rls = mergeRoles(roles);
		String[] ids = new String[rls.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = SnowflakeUtil.asString(rls.get(i));
		}

		JSONObject payload = new JSONObject().put("roles", ids);
		System.out.println(payload);
		CompletableFuture<Void> vcf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		vcf.thenAcceptAsync(v -> {
			getLoader().addEventListener(new EventListenerAdapter() {
				public void GuildMemberUpdate(GuildMemberUpdateEvent e) {
					future.complete(e.getMember());
					getLoader().removeEventListener(this);
				}
			});
		});
		vcf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});

		return future;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}

	@Override
	public boolean hasRole(IRole role) {
		if (role != null) {
			return hasRole(role.getID());
		}
		return false;
	}

	@Override
	public boolean hasRole(long roleID) {
		for (IRole role : getRoles()) {
			if (role.getID() == roleID) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasRole(String roleID) {
		return hasRole(SnowflakeUtil.parse(roleID));
	}

	/**
	 * Determines if the member is connected to a voice channel in their
	 * {@link #guild}
	 * 
	 * @return {@code true} if the member has a {@link VoiceState}, {@code false}
	 *         otherwise.
	 */
	public boolean hasVoiceConnection() {
		return getVoiceState() != null;
	}

	@Override
	public CompletableFuture<Boolean> isBanned() {
		CompletableFuture<Boolean> future = new CompletableFuture<>();
		CompletableFuture<List<IGuildBan>> cf = getGuild().fetchBans();
		cf.thenAcceptAsync(bans -> {
			for (IGuildBan ban : bans) {
				if (ban.getUser().getID() == getUser().getID()) {
					future.complete(true);
					return;
				}
			}
			future.complete(false);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return new ArrayList<>();
		});
		return future;
	}

	@Override
	public boolean isDeafened() {
		return hasVoiceConnection() ? (getVoiceState().deaf || deaf) : deaf;
	}

	@Override
	public boolean isMuted() {
		return hasVoiceConnection() ? (getVoiceState().mute || mute) : mute;
	}

	@Override
	public boolean isOwner() {
		return guild.isOwner(this);
	}

	/**
	 * Kicks the member from the {@link Guild} if the {@link DiscLoader client} has
	 * sufficient permissions
	 * 
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException
	 *             Thrown if the current user doesn't have the
	 *             {@link Permissions#KICK_MEMBERS} permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> kick() {
		return guild.kick(this);
	}

	@Override
	public CompletableFuture<IGuildMember> kick(String reason) {
		return guild.kick(this, reason);
	}

	protected List<IRole> mergeRoles(IRole... roles) {
		List<IRole> rls = getRoles();
		for (int i = 0; i < roles.length; i++) {
			if (!hasRole(roles[i])) {
				rls.add(roles[i]);
			}
		}
		return rls;
	}

	/**
	 * Move the {@link GuildMember member} to a different {@link VoiceChannel} if
	 * they are already connected to a {@link VoiceChannel}.
	 * 
	 * @param channel
	 *            The {@link VoiceChannel} to move the member to.
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException
	 *             thrown if the current user doesn't have the
	 *             {@link Permissions#MOVE_MEMBERS} permission.
	 * @throws VoiceConnectionException
	 *             Thrown if the member is not in a voice channel.
	 */
	@Override
	public CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel) {
		if (!equals(guild.getCurrentMember()) && !guild.isOwner() && !channel.permissionsOf(guild.getCurrentMember()).hasPermission(Permissions.MOVE_MEMBERS))
			throw new PermissionsException("Insufficient Permissions");
		if (getVoiceChannel() == null)
			throw new VoiceConnectionException();

		return new ModifyMember(this, channel).execute();
	}

	@Override
	public CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel, String reason) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<IGuildMember>();
		JSONObject payload = new JSONObject();
		if (channel != null) {
			payload.put("channel_id", SnowflakeUtil.asString(channel));
		}
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		cf.thenAcceptAsync(v -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Server deafens a {@link GuildMember} if they are not already server deafened
	 * 
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException
	 *             thrown if the current user doesn't have the
	 *             {@link Permissions#MUTE_MEMBERS} permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> mute() {
		if (!guild.hasPermission(Permissions.MUTE_MEMBERS)) {
			throw new PermissionsException("Insufficient Permissions");
		}

		return new ModifyMember(this, nick, getRoles(), true, deaf, getVoiceChannel()).execute();
	}

	@Override
	public CompletableFuture<IGuildMember> mute(String reason) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<IGuildMember>();
		JSONObject payload = new JSONObject().put("mute", true);
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		cf.thenAcceptAsync(v -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Sets the member's nickname if the {@link DiscLoader loader} has sufficient
	 * permissions. Requires the {@link Permissions#MANAGE_NICKNAMES} permission.
	 * 
	 * @param nick
	 *            The member's new nickname
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException
	 *             thrown if the current user doesn't have the
	 *             {@link Permissions#MANAGE_NICKNAMES} permission.
	 */
	@Override
	public CompletableFuture<IGuildMember> setNick(String nick) {
		if ((equals(guild.getCurrentMember()) && !guild.hasPermission(Permissions.CHANGE_NICKNAME)) || (!equals(guild.getCurrentMember()) && !guild.hasPermission(Permissions.MANAGE_NICKNAMES))) {
			throw new PermissionsException("Insuccficient Permissions");
		}

		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("nick", nick);
		String endpoint = equals(guild.getCurrentMember()) ? Endpoints.guildNick(guild.getID()) : Endpoints.guildMember(guild.getID(), getID());
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.PATCH, endpoint, new RESTOptions(payload), Void.class);
		IEventListener iel = new EventListenerAdapter() {
			public void GuildMemberNicknameUpdated(NicknameUpdateEvent e) {
				if (e.getMember().getID() == getID() && e.getGuild().equals(guild)) {
					future.complete(e.getMember());
					getLoader().removeEventListener(this);
				}
			}
		};
		getLoader().addEventListener(iel);
		cf.exceptionally(ex -> {
			getLoader().removeEventListener(iel);
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildMember> setNick(String nick, String reason) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<IGuildMember>();
		return future;
	}

	@Override
	public CompletableFuture<IGuildMember> takeRole(IRole... roles) {
		if (!guild.hasPermission(Permissions.MANAGE_ROLES)) {
			throw new PermissionsException("Insuccficient Permissions");
		}

		List<IRole> rls = getRoles();

		for (IRole role : roles) {
			if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) {
				throw new PermissionsException("Cannot take away roles higher than your's");
			}

			if (hasRole(role)) {
				rls.remove(role);
			}
		}

		CompletableFuture<IGuildMember> future = new CompletableFuture<>();

		String[] ids = new String[rls.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = SnowflakeUtil.asString(rls.get(i));
		}

		JSONObject payload = new JSONObject().put("roles", ids);
		CompletableFuture<Void> tcf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(payload), Void.class);
		IEventListener iel = new EventListenerAdapter() {
			public void GuildMemberUpdate(GuildMemberUpdateEvent e) {
				if (e.getMember().getID() == getID()) {
					future.complete(e.getMember());
					getLoader().removeEventListener(this);
				}
			}
		};
		getLoader().addEventListener(iel);
		tcf.exceptionally(ex -> {
			getLoader().removeEventListener(iel);
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildMember> takeRole(String reason, IRole... roles) {
		if (!guild.hasPermission(Permissions.MANAGE_ROLES)) {
			throw new PermissionsException("Insuccficient Permissions");
		}

		List<IRole> rls = getRoles();

		for (IRole role : roles) {
			if (!guild.isOwner() && role.getPosition() >= guild.getCurrentMember().getHighestRole().getPosition()) {
				throw new PermissionsException("Cannot take away roles higher than your's");
			}

			if (hasRole(role)) {
				rls.remove(role);
			}
		}

		CompletableFuture<IGuildMember> future = new CompletableFuture<>();

		String[] ids = new String[rls.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = SnowflakeUtil.asString(rls.get(i));
		}

		JSONObject payload = new JSONObject().put("roles", ids);
		CompletableFuture<Void> tcf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		IEventListener iel = new EventListenerAdapter() {
			public void GuildMemberUpdate(GuildMemberUpdateEvent e) {
				if (e.getMember().getID() == getID()) {
					future.complete(e.getMember());
					getLoader().removeEventListener(this);
				}
			}
		};
		getLoader().addEventListener(iel);
		tcf.exceptionally(ex -> {
			getLoader().removeEventListener(iel);
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Same as {@link User#toString()}
	 * 
	 * @return a string in mention format
	 */
	@Override
	public String toMention() {
		return String.format("<@!%s>", getID());
	}

	@Override
	public String toString() {
		return String.format("%s#%04d", getName(), user.getDiscriminator());
	}

	@Override
	public CompletableFuture<IGuildMember> unDeafen() {
		return new ModifyMember(this, nick, getRoles(), mute, false, getVoiceChannel()).execute();
	}

	@Override
	public CompletableFuture<IGuildMember> unDeafen(String reason) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<IGuildMember>();
		JSONObject payload = new JSONObject().put("deaf", false);
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		cf.thenAcceptAsync(v -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildMember> unMute() {
		return new ModifyMember(this, nick, getRoles(), false, deaf, getVoiceChannel()).execute();
	}

	@Override
	public CompletableFuture<IGuildMember> unMute(String reason) {
		CompletableFuture<IGuildMember> future = new CompletableFuture<IGuildMember>();
		JSONObject payload = new JSONObject().put("mute", false);
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.PATCH, Endpoints.guildMember(getGuild().getID(), getID()), new RESTOptions(true, payload, reason), Void.class);
		cf.thenAcceptAsync(v -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public void setRoles(String[] roleIDs) {
		this.roleIDs = roleIDs;
	}

}
