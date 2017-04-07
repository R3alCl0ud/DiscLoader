package io.discloader.discloader.core.entity.guild;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.client.render.texture.icon.GuildSplash;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.exceptions.MissmatchException;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.Presence;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IInvite;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.guild.VoiceRegion;
import io.discloader.discloader.entity.invite.Invite;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.EmojiJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.json.VoiceStateJSON;
import io.discloader.discloader.network.rest.actions.guild.ModifyGuild;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;

/**
 * This represents a Guild in Discord's API
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class Guild implements IGuild {

	/**
	 * The guild's Snowflake ID.
	 */
	private final String id;

	/**
	 * The guild's name
	 */
	public String name;

	/**
	 * The guild's owner's Snowflake ID.
	 */
	public String ownerID;

	/**
	 * The hash code of the guild's icon
	 */
	public String icon;

	/**
	 * The url to where the {@link #icon} is located
	 */
	public String iconURL;

	/**
	 * The guild's splash screen
	 */
	public GuildSplash splash;

	/**
	 * The hash of the guild splash.
	 */
	public String splashHash;

	/**
	 * The amount of members in the guild, {@link #members members.size()} maybe
	 * not be equal to {@link #memberCount} if this is a
	 * {@link #isLarge() large} {@link Guild guild}, as {@link #members} is a
	 * map of the currently cached {@link GuildMember guild members}.
	 */
	private int memberCount;

	/**
	 * Whether or not the guild is currently available
	 */
	public boolean available;

	private String afk_channel_id;

	/**
	 * The instance of the {@link DiscLoader loader} the cached the guild
	 */
	private final DiscLoader loader;

	/**
	 * A HashMap of the guild's cached members. Indexed by member ID.
	 * 
	 * @see GuildMember
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<Long, IGuildMember> members;

	/**
	 * A HashMap of the guild's TextChannels. Indexed by channel ID.
	 * 
	 * @see TextChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	private Map<Long, IGuildTextChannel> textChannels;

	/**
	 * A HashMap of the guild's VoiceChannels. Indexed by channel ID.
	 * 
	 * @see VoiceChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	private HashMap<Long, IGuildVoiceChannel> voiceChannels;

	/**
	 * A HashMap of the guild's roles. Indexed by role ID.
	 * 
	 * @see Role
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<Long, IRole> roles;

	/**
	 * A HashMap of the presences of the guild's members
	 * 
	 * @see GuildMember
	 * @see Presence
	 * @see HashMap
	 * @author Perry Berman
	 */
	public Map<Long, IPresence> presences;

	/**
	 * A HashMap of the guild's custom emojis. Indexed by {@link Emoji#id}
	 * 
	 * @author Perry Berman
	 */
	public Map<Long, IGuildEmoji> emojis;

	/**
	 * A Private HashMap of the guild's raw voice states. Indexed by
	 * {@link GuildMember#id}
	 * 
	 * @author Perry Berman
	 */
	private HashMap<Long, VoiceState> rawStates;

	/**
	 * The guild's current voice region
	 */
	private VoiceRegion voiceRegion;

	/**
	 * Creates a new guild
	 * 
	 * @param loader The current instance of DiscLoader
	 * @param data The guild's data
	 */
	public Guild(DiscLoader loader, GuildJSON data) {
		this.loader = loader;

		this.members = new HashMap<>();
		this.textChannels = new HashMap<>();
		this.voiceChannels = new HashMap<>();
		this.roles = new HashMap<>();
		this.presences = new HashMap<>();
		this.emojis = new HashMap<>();
		this.rawStates = new HashMap<>();
		this.voiceRegion = new VoiceRegion("us-central");

		if (data.unavailable == true) {
			this.available = false;
			this.id = data.id;
		} else {
			this.available = true;
			this.id = data.id;
			this.setup(data);
		}
	}

	/**
	 * Method used internally by DiscLoader to make a new {@link GuildMember}
	 * object when a member's data is recieved
	 * 
	 * @param data The member's data
	 * @return The {@link GuildMember} that was instantiated.
	 */
	@Override
	public IGuildMember addMember(MemberJSON data) {
		return this.addMember(data, false);
	}

	/**
	 * Method used internally by DiscLoader to make a new {@link GuildMember}
	 * object when a member's data is recieved
	 * 
	 * @param data The member's data
	 * @param shouldEmit if a {@code GuildMemberAddEvent} should be fired by the
	 *            client
	 * @return The {@link GuildMember} that was instantiated.
	 */
	public IGuildMember addMember(MemberJSON data, boolean shouldEmit) {
		boolean exists = members.containsKey(data.user.id);
		IGuildMember member = new GuildMember(this, data);
		members.put(member.getID(), member);

		if (!exists && loader.ready && shouldEmit) {
			GuildMemberAddEvent event = new GuildMemberAddEvent(member);
			loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
			loader.emit(event);
		}
		return member;
	}

	/**
	 * Method used internally by DiscLoader to make a new {@link GuildMember}
	 * object when a member's data is recieved
	 * 
	 * @param user the member's {@link User} object.
	 * @param roles the member's role's ids.
	 * @param deaf is the member deafened.
	 * @param mute is the member muted.
	 * @param nick The member's nickname.
	 * @param emitEvent if a {@code GuildMemberAddEvent} should be fired by the
	 *            client.
	 * @return The {@link GuildMember} that was instantiated.
	 */
	@Override
	public GuildMember addMember(IUser user, String[] roles, boolean deaf, boolean mute, String nick, boolean emitEvent) {
		boolean exists = this.members.containsKey(user.getID());
		GuildMember member = new GuildMember(this, user, roles, deaf, mute, nick);
		this.members.put(member.getID(), member);
		if (this.loader.ready == true && emitEvent && !exists) {
			GuildMemberAddEvent event = new GuildMemberAddEvent(member);
			this.loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
			for (IEventListener e : loader.handlers) {
				e.GuildMemberAdd(event);
			}
		}

		return member;
	}

	@Override
	public Role addRole(RoleJSON guildRole) {
		boolean exists = this.roles.containsKey(guildRole.id);
		Role role = new Role(this, guildRole);
		this.roles.put(role.getID(), role);
		if (!exists && this.loader.ready) {
			this.loader.emit(DLUtil.Events.GUILD_ROLE_CREATE, role);
		}
		return role;
	}

	/**
	 * Bans the member from the {@link Guild} if the {@link DiscLoader loader}
	 * has sufficient permissions
	 *
	 * @param member The member to ban from the guild
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 */
	@Override
	public CompletableFuture<IGuildMember> ban(IGuildMember member) {
		return this.loader.rest.banMember(this, member);
	}

	/**
	 * Begin a prune operation. Requires the 'KICK_MEMBERS' permission.
	 * 
	 * @return A Future that completes with the number of member kicked during
	 *         the prune operation if successful.
	 */
	public CompletableFuture<Integer> beginPrune() {
		return beginPrune(1);
	}

	/**
	 * Begin a prune operation. Requires the 'KICK_MEMBERS' permission.
	 * 
	 * @param days The number of days to prune (1 or more)
	 * @return A Future that completes with the number of member kicked during
	 *         the prune operation if successful.
	 */
	public CompletableFuture<Integer> beginPrune(int days) {
		return this.loader.rest.beginPrune(this, days);
	}

	@Override
	public CompletableFuture<IGuildChannel> createChannel(String name, String type) {
		return null;
	}

	/**
	 * Creates a new custom emoji
	 * 
	 * @param name The name of the new emoji
	 * @param image The file
	 * @return A Future the completes with the created Emoji if successful.
	 */
	public CompletableFuture<Emoji> createEmoji(String name, File image) {
		String base64 = null;
		try {
			base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(image.toPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.createEmoji(name, base64);
	}

	/**
	 * Creates a new custom emoji
	 * 
	 * @param name The name of the emoji
	 * @param image The emoji's image encoded to base64
	 * @return A Future the completes with the created Emoji if successful.
	 */
	public CompletableFuture<Emoji> createEmoji(String name, String image) {
		return this.loader.rest.createEmoji(this, name, image);
	}

	/**
	 * Creates a new {@link Role}.
	 * 
	 * @return A future that completes with a new {@link Role} Object if
	 *         successful.
	 * @since 0.0.3
	 */
	public CompletableFuture<Role> createRole() {
		return this.createRole(null, 0, 0, false, false);
	}

	/**
	 * Creates a new {@link Role}.
	 * 
	 * @param name The name of the role
	 * @param permissions The 53bit Permissions integer to assign to the role
	 * @param color The color of the role
	 * @return A future that completes with a new {@link Role} Object if
	 *         successful.
	 * @since 0.0.3
	 */
	public CompletableFuture<Role> createRole(String name, int permissions, int color) {
		return this.createRole(name, permissions, color, false, false);
	}

	/**
	 * Creates a new {@link Role}.
	 * 
	 * @param name The name of the role
	 * @param permissions The 53bit Permissions integer to assign to the role
	 * @param color The color of the role
	 * @param hoist Display role members separately from online members
	 * @param mentionable Allow anyone to @mention this role
	 * @return A future that completes with a new {@link Role} Object if
	 *         successful.
	 * @since 0.0.3
	 */
	public CompletableFuture<Role> createRole(String name, int permissions, int color, boolean hoist, boolean mentionable) {
		return loader.rest.createRole(this, name, permissions, color, hoist, mentionable);
	}

	/**
	 * Creates a new {@link TextChannel}.
	 * 
	 * @param name The name of the channel
	 * @return A Future that completes with a {@link TextChannel} if successful.
	 */
	public CompletableFuture<TextChannel> createTextChannel(String name) {
		return this.loader.rest.createTextChannel(this, new JSONObject().put("name", name));
	}

	/**
	 * Creates a new {@link VoiceChannel}
	 * 
	 * @param name The channel's name
	 * @return A future that completes with a new {@link VoiceChannel} Object if
	 *         successful.
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name) {
		return this.loader.rest.createVoiceChannel(this, new JSONObject().put("name", name));
	}

	/**
	 * Creates a new {@link VoiceChannel}
	 * 
	 * @param name The name of the channel
	 * @param bitrate The channel's bitrate
	 * @return A future that completes with a new {@link VoiceChannel} Object if
	 *         successful.
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name, int bitrate) {
		return this.loader.rest.createVoiceChannel(this, new JSONObject().put("name", name).put("bitrate", bitrate));
	}

	/**
	 * Creates a new {@link VoiceChannel}
	 * 
	 * @param name The name of the channel
	 * @param bitrate The channel's bitrate
	 * @param userLimit the channel's userlimit
	 * @return A future that completes with a new {@link VoiceChannel} Object if
	 *         successful.
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name, int bitrate, int userLimit) {
		return this.loader.rest.createVoiceChannel(this, new JSONObject().put("name", name).put("bitrate", bitrate).put("user_limit", userLimit));
	}

	/**
	 * Deletes the Guild if the user you have logged in as is the owner of the
	 * guild
	 * 
	 * @return A Future that completes with {@code this} if successful, and
	 *         fails with a {@link UnauthorizedException}
	 * @throws UnauthorizedException Thrown if you are not the owner of the
	 *             guild.
	 */
	public CompletableFuture<Guild> delete() {
		if (!isOwner()) throw new UnauthorizedException("Only the guild's owner can delete a guild");
		CompletableFuture<Guild> future = new CompletableFuture<Guild>();
		loader.rest.makeRequest(DLUtil.Endpoints.guild(getID()), DLUtil.Methods.DELETE, true).thenAcceptAsync(data -> {
			future.complete(this);
		});
		return future;
	}

	/**
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException Thrown if the current user doesn't have the
	 *             {@link Permissions#MANAGE_GUILD} permission.
	 */
	public CompletableFuture<Guild> edit() {
		if (!isOwner() && getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException();
		return null;
	}

	/**
	 * @return {@code true} if all fields are equivalent, {@code false}
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Guild)) return false;
		Guild guild = (Guild) object;
		if (!guild.id.equals(id)) return false;
		for (IRole role : roles.values())
			if (!guild.roles.containsKey(role.getID())) return false;
		for (IGuildMember member : members.values())
			if (!guild.members.containsKey(member.getID())) return false;
		return guild.name.equals(name) && guild.ownerID.equals(ownerID) && guild.icon.equals(icon) && (isSyncing() == guild.isSyncing());
	}

	/**
	 * Fetches a GuildMember from the REST API
	 * 
	 * @param memberID the ID of the member to load
	 * @return CompletableFuture.GuildMember
	 */
	public CompletableFuture<IGuildMember> fetchMember(long memberID) {
		return loader.rest.loadGuildMember(this, memberID);
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild.
	 * 
	 * @param limit max number of members to return (1-1000) default 50
	 * @param after The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers
	 *         if successful, null otherwise.
	 */
	public CompletableFuture<HashMap<Long, IGuildMember>> fetchMembers(int limit, long after) {
		return loader.rest.loadGuildMembers(this, limit, after);
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild. <u>Only retrieves
	 * 50 members</u>
	 * 
	 * @param after The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers
	 *         if successful, null otherwise.
	 */
	public CompletableFuture<HashMap<Long, IGuildMember>> fetchMembers(long after) {
		return loader.rest.loadGuildMembers(this, 50, after);
	}

	/**
	 * @return the afk_channel_id
	 */
	public IVoiceChannel getAfkChannel() {
		return voiceChannels.get(afk_channel_id);
	}

	/**
	 * Returns the {@link GuildMember} object for the {@link User} you are
	 * logged in as.
	 * 
	 * @return A GuildMember object
	 */
	@Override
	public IGuildMember getCurrentMember() {
		return members.get(loader.user.getID());
	}

	/**
	 * Gets the guild's default text channel. the {@link Channel#id id} of the
	 * channel should be the same as the guild's {@link #id}
	 * 
	 * @return the default TextChannel
	 */
	public ITextChannel getDefaultChannel() {
		return textChannels.get(id);
	}

	@Override
	public Map<Long, IGuildEmoji> getEmojis() {
		return emojis;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return SnowflakeUtil.parse(id);
	}

	/**
	 * Retrieves the guild's invites from Discord's API
	 * 
	 * @return A Future that completes with a HashMap of Invite objects, indexed
	 *         by {@link Invite#code}, if successful.
	 */
	public CompletableFuture<HashMap<Long, IInvite>> getInvites() {
		CompletableFuture<HashMap<Long, IInvite>> future = new CompletableFuture<>();
		HashMap<Long, IInvite> invites = new HashMap<>();
		loader.rest.getInvites(this).thenAcceptAsync(action -> {
			for (InviteJSON i : action) {
				invites.put(SnowflakeUtil.parse(i.code), new Invite(i, loader));
			}
			future.complete(invites);
		});
		return future;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	@Override
	public int getMemberCount() {
		return memberCount;
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#getMembers()
	 */
	@Override
	public Map<Long, IGuildMember> getMembers() {
		return members;
	}

	/**
	 * Returns a {@link GuildMember} object repersenting the guild's owner
	 * 
	 * @return A {@link GuildMember} object
	 */
	public IGuildMember getOwner() {
		return members.get(ownerID);
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#getPresences()
	 */
	@Override
	public Map<Long, IPresence> getPresences() {
		return presences;
	}

	/**
	 * same as {@link #getPruneCount(int days)} but only grabs one days worth
	 * 
	 * @return A Future that completes with the number of member that would be
	 *         kicked in a prune operation
	 */
	public CompletableFuture<Integer> getPruneCount() {
		return getPruneCount(1);
	}

	/**
	 * Returns the number of members that would be removed in a prune operation.
	 * Requires the KICK_MEMBERS permission.
	 * 
	 * @param days The number of days to count prune for (1 or more)
	 * @return A Future that completes with the number of member that would be
	 *         kicked in a prune operation
	 */
	public CompletableFuture<Integer> getPruneCount(int days) {
		return loader.rest.pruneCount(this, days);
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#getRoles()
	 */
	@Override
	public Map<Long, IRole> getRoles() {
		return roles;
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#getTextChannels()
	 */
	@Override
	public Map<Long, IGuildTextChannel> getTextChannels() {
		return textChannels;
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#getVoiceChannels()
	 */
	@Override
	public Map<Long, IGuildVoiceChannel> getVoiceChannels() {
		return voiceChannels;
	}

	/**
	 * @return the voiceRegion
	 */
	@Override
	public VoiceRegion getVoiceRegion() {
		return voiceRegion;
	}

	/**
	 * returns a HashMap of the guild's members' voice states. Indexed by
	 * {@link GuildMember#id}
	 * 
	 * @return A HashMap of {@link VoiceState VoiceStates}
	 */
	@Override
	public HashMap<Long, VoiceState> getVoiceStates() {
		return rawStates;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return available;
	}

	/**
	 * Whether or not the guild has at least 250 members.
	 * 
	 * @return {@code if} {@link #memberCount} is greater than or equal to 250,
	 *         {@code false} otherwise.
	 */
	public boolean isLarge() {
		return memberCount >= 250;
	}

	/**
	 * @return {@code true} if {@link #getCurrentMember()}{@link GuildMember#id
	 *         .id} is equal to {@link #ownerID}, false otherwise.
	 */
	@Override
	public boolean isOwner() {
		return isOwner(getCurrentMember());
	}

	/**
	 * @param iGuildMember The member to check if their the {@link Guild
	 *            guild's} owner
	 * @return {@code true} if {@link GuildMember#id} is equal to
	 *         {@link #ownerID}, false otherwise.
	 */
	@Override
	public boolean isOwner(IGuildMember iGuildMember) {
		return getOwner().getID() == iGuildMember.getID();
	}

	/**
	 * Checks if the guild is currently being synced with discord
	 * 
	 * @return {@code true} if syncing, false otherwise.
	 */
	public boolean isSyncing() {
		return loader.isGuildSyncing(this);
	}

	public CompletableFuture<IGuildMember> kickMember(IGuildMember guildMember) {
		if (!isOwner() && getCurrentMember().getPermissions().hasPermission(Permissions.KICK_MEMBERS)) throw new PermissionsException();

		return loader.rest.removeMember(this, guildMember);
	}

	/**
	 * Makes the client leave the guild
	 * 
	 * @return A Future that completes with {@code this} if successful.
	 */
	public CompletableFuture<Guild> leave() {
		CompletableFuture<Guild> future = new CompletableFuture<>();
		this.kickMember(getCurrentMember()).thenAcceptAsync(action -> {
			future.complete(this);
		});

		return future;
	}

	/**
	 * @param channel The {@link VoiceChannel} to set as the afk channel.
	 * @throws PermissionsException
	 * @throws MissmatchException
	 */
	public CompletableFuture<Guild> setAfkChannel(VoiceChannel channel) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		if (!id.equals(channel.getGuild().getID())) throw new MissmatchException("Afk Channel cannot be set to a voice channel from another guild");
		return new ModifyGuild(this, new JSONObject().put("afk_channel_id", channel.getID())).execute();
	}

	/**
	 * Sets the guild's icon if the loader has sufficient permissions
	 * 
	 * @param icon location of icon file on disk
	 * @return A Future that completes with {@code this} if successful.
	 * @throws IOException Exception thrown if there is an error reading icon
	 *             file
	 * @throws PermissionsException
	 */
	public CompletableFuture<Guild> setIcon(String icon) throws IOException {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		String base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(icon))));
		return new ModifyGuild(this, new JSONObject().put("icon", base64)).execute();
	}

	/**
	 * Sets the guild's name if the loader has sufficient permissions
	 * 
	 * @param name The guild's new name
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException
	 */
	public CompletableFuture<Guild> setName(String name) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		return new ModifyGuild(this, new JSONObject().put("name", name)).execute();
	}

	/**
	 * @param owner
	 * @return A Future that completes with {@code this} if successful.
	 * @throws UnauthorizedException Thrown if you are not the owner of theS
	 *             guild.
	 */
	public CompletableFuture<Guild> setOwner(GuildMember owner) {
		if (!isOwner()) throw new UnauthorizedException("Only the guild's owner can delete a guild");
		return new ModifyGuild(this, new JSONObject().put("owner_id", owner.getID())).execute();
	}

	@Override
	public void setPresence(PresenceJSON guildPresence) {
		setPresence(guildPresence, false);
	}

	public void setPresence(PresenceJSON guildPresence, boolean shouldEmit) {
		IPresence presence = new Presence(guildPresence);
		if (guildPresence.user.id.equals(this.loader.user.getID())) loader.user.presence.update(guildPresence);
		presences.put(SnowflakeUtil.parse(guildPresence.user.id), presence);
	}

	/**
	 * Sets up a guild with data from the gateway
	 * 
	 * @param data The guild's data
	 */
	@Override
	public void setup(GuildJSON data) {
		try {
			name = data.name;
			icon = data.icon != null ? data.icon : null;
			iconURL = icon != null ? Endpoints.guildIcon(getID(), icon) : null;
			ownerID = data.owner_id;
			memberCount = data.member_count;
			voiceRegion = new VoiceRegion(data.region);
			splashHash = data.splash;
			ProgressLogger.step(1, 7, "Caching Roles");
			if (data.roles.length > 0) {
				this.roles.clear();
				for (RoleJSON role : data.roles) {
					this.addRole(role);
				}
			}
			ProgressLogger.step(2, 7, "Caching Members");
			if (data.members != null && data.members.length > 0) {
				this.members.clear();
				for (MemberJSON member : data.members) {
					this.addMember(member);
				}
			}
			ProgressLogger.step(3, 7, "Caching Channels");
			if (data.channels != null && data.channels.length > 0) {
				for (ChannelJSON channel : data.channels) {
					this.loader.addChannel(channel, this);
				}
			}
			ProgressLogger.step(4, 7, "Caching Presences");
			if (data.presences != null && data.presences.length > 0) {
				this.presences.clear();
				for (PresenceJSON presence : data.presences) {
					this.setPresence(presence);
				}
			}
			ProgressLogger.step(5, 7, "Caching Emojis");
			if (data.emojis != null && data.emojis.length > 0) {
				this.emojis.clear();
				for (EmojiJSON e : data.emojis) {
					this.emojis.put(SnowflakeUtil.parse(e.id), new Emoji(e, this));
				}
			}
			ProgressLogger.step(6, 7, "Caching Voice States");
			if (data.voice_states != null && data.voice_states.length > 0) {
				this.rawStates.clear();
				for (VoiceStateJSON v : data.voice_states) {
					this.rawStates.put(SnowflakeUtil.parse(v.user_id), new VoiceState(v, this));
				}
			}

			ProgressLogger.step(7, 7, "Registering Icon");
			try {
				TextureRegistry.registerGuildIcon(new GuildIcon(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.available = data.unavailable == true ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the Guild's voice region to the specified region
	 * 
	 * @param region The new voice region
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException
	 */
	public CompletableFuture<Guild> setVoiceRegion(String region) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		return new ModifyGuild(this, new JSONObject().put("region", region)).execute();
	}

	/**
	 * Syncs the guild to the client if the logged in user is not a bot.
	 * 
	 * @throws GuildSyncException Thrown if {@link #isSyncing()} returns true.
	 * @throws AccountTypeException Thrown if client is logged in as a bot
	 *             account
	 */
	public void sync() throws GuildSyncException, AccountTypeException {
		loader.syncGuilds(this.id);
	}

	@Override
	public void updateVoiceState(VoiceState state) {
		rawStates.put(state.member.getID(), state);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * io.discloader.discloader.entity.guild.IGuild#getMember(java.lang.String)
	 */
	@Override
	public IGuildMember getMember(long memberID) {
		return members.get(memberID);
	}

	@Override
	public IPresence getPresence(long memberID) {
		return presences.get(memberID);
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.guild.IGuild#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasPermission(Permissions permissions) {
		return isOwner() || getCurrentMember().getPermissions().hasPermission(permissions);
	}

}