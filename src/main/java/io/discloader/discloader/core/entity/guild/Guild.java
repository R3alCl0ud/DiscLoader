package io.discloader.discloader.core.entity.guild;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.client.render.texture.icon.GuildSplash;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.exceptions.MissmatchException;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.core.entity.Presence;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.invite.Invite;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IIntegration;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.guild.VoiceRegion;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.SendableRole;
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
import io.discloader.discloader.network.rest.actions.guild.CreateRole;
import io.discloader.discloader.network.rest.actions.guild.ModifyGuild;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;

public class Guild implements IGuild {

	public class MemberQuery {

		public String guild_id = Long.toUnsignedString(getID(), 10);
		public int limit;
		public String query;

		public MemberQuery(int limit, String query) {
			this.limit = limit;
			this.query = query;
		}
	}

	private static int i = 0;

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
	public long ownerID;

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
	 * {@link GuildMember#getID}
	 * 
	 * @author Perry Berman
	 */
	private HashMap<Long, VoiceState> rawStates;

	/**
	 * The guild's current voice region
	 */
	private VoiceRegion voiceRegion;

	private GuildFactory gfac = EntityBuilder.getGuildFactory();

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

	@Override
	public IGuildMember addMember(IGuildMember member) {
		return addMember(member, false);
	}

	@Override
	public IGuildMember addMember(IGuildMember member, boolean emit) {
		members.put(member.getID(), member);
		if (emit) {
			memberCount++;
			loader.emit(new GuildMemberAddEvent(member));
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
		boolean exists = members.containsKey(user.getID());
		GuildMember member = new GuildMember(this, user, roles, deaf, mute, nick);
		members.put(member.getID(), member);
		if (loader.ready == true && emitEvent && !exists) {
			memberCount++;
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
		boolean exists = members.containsKey(SnowflakeUtil.parse(data.user.id));
		IGuildMember member = new GuildMember(this, data);
		members.put(member.getID(), member);

		if (!exists && shouldEmit) {
			memberCount++;
			GuildMemberAddEvent event = new GuildMemberAddEvent(member);
			loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
			loader.emit(event);
		}
		return member;
	}

	@Override
	public IRole addRole(IRole role) {
		roles.put(role.getID(), role);
		return role;
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

	@Override
	public CompletableFuture<IGuildMember> ban(IGuildMember member) {
		return loader.rest.banMember(this, member);
	}

	public CompletableFuture<Integer> beginPrune() {
		return beginPrune(1);
	}

	public CompletableFuture<Integer> beginPrune(int days) {
		return loader.rest.beginPrune(this, days);
	}

	// @Override
	// public CompletableFuture<IGuildChannel> createChannel(String name, String
	// type) {
	// return null;
	// }

	@Override
	public OffsetDateTime createdAt() {
		return SnowflakeUtil.creationTime(this);
	}

	public CompletableFuture<IGuildEmoji> createEmoji(String name, File image) {
		String base64 = null;
		try {
			base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(image.toPath())));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.createEmoji(name, base64);
	}

	public CompletableFuture<IGuildEmoji> createEmoji(String name, String image) {
		return this.loader.rest.createEmoji(this, name, image);
	}

	/**
	 * Creates a new {@link Role}.
	 * 
	 * @return A future that completes with a new {@link Role} Object if
	 *         successful.
	 * @since 0.0.3
	 */
	public CompletableFuture<IRole> createRole() {
		return this.createRole(null, 0, 0, false, false);
	}

	@Override
	public CompletableFuture<IRole> createRole(String name) {
		return new CreateRole(this, new SendableRole(name, 0, 0, false, false)).execute();
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
	public CompletableFuture<IRole> createRole(String name, int permissions, int color) {
		return this.createRole(name, permissions, color, false, false);
	}

	public CompletableFuture<IRole> createRole(String name, int permissions, int color, boolean hoist, boolean mentionable) {
		return null;
		// loader.rest.createRole(this, name, permissions, color, hoist,
		// mentionable);
	}

	/**
	 * Creates a new {@link TextChannel}.
	 * 
	 * @param name The name of the channel
	 * @return A Future that completes with a {@link TextChannel} if successful.
	 */
	public CompletableFuture<IGuildTextChannel> createTextChannel(String name) {
		return null;
		// this.loader.rest.createTextChannel(this, new JSONObject().put("name",
		// name));
	}

	@Override
	public CompletableFuture<IGuildTextChannel> createTextChannel(String name, IOverwrite... overwrites) {
		return null;
	}

	/**
	 * Creates a new {@link VoiceChannel}
	 * 
	 * @param name The channel's name
	 * @return A future that completes with a new {@link VoiceChannel} Object if
	 *         successful.
	 */
	public CompletableFuture<IGuildVoiceChannel> createVoiceChannel(String name) {
		return null;
		// this.loader.rest.createVoiceChannel(this, new
		// JSONObject().put("name", name));
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

	@Override
	public CompletableFuture<IGuildTextChannel> createVoiceChannel(String name, int bitRate, IOverwrite... overwrites) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> createVoiceChannel(String name, IOverwrite... overwrites) {
		return null;
	}

	public CompletableFuture<IGuild> delete() {
		if (!isOwner()) throw new UnauthorizedException("Only the guild's owner can delete a guild");
		CompletableFuture<IGuild> future = new CompletableFuture<>();
		loader.rest.makeRequest(Endpoints.guild(getID()), DLUtil.Methods.DELETE, true).thenAcceptAsync(data -> {
			future.complete(this);
		});
		return future;
	}

	public CompletableFuture<IGuild> edit(String name, String icon, IGuildVoiceChannel afkChannel) throws IOException {
		if (!isOwner() && getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException();
		return new CompletableFuture<>();
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
		return guild.name.equals(name) && guild.ownerID == ownerID && guild.icon.equals(icon) && (isSyncing() == guild.isSyncing());
	}

	public CompletableFuture<IGuildMember> fetchMember(long memberID) {
		return loader.rest.loadGuildMember(this, memberID);
	}

	public CompletableFuture<Map<Long, IGuildMember>> fetchMembers() {
		return fetchMembers((int) 0);
	}

	@Override
	public CompletableFuture<Map<Long, IGuildMember>> fetchMembers(int limit) {
		CompletableFuture<Map<Long, IGuildMember>> future = new CompletableFuture<>();
		final Consumer<DLEvent> consumer = event -> {
			if (event instanceof GuildMembersChunkEvent) {
				i++;
				System.out.println(getID() + " " + i);
				GuildMembersChunkEvent gmce = (GuildMembersChunkEvent) event;
				future.complete(gmce.members);
			}
		};
		loader.onceEvent(consumer, guild -> guild.getID() == getID());
		Packet payload = new Packet(8, new MemberQuery(limit, ""));
		System.out.println(DLUtil.gson.toJson(payload));
		loader.socket.send(payload);
		return future;
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild.
	 * 
	 * @param limit max number of members to return (1-1000) default 50
	 * @param after The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers
	 *         if successful, null otherwise.
	 */
	public CompletableFuture<Map<Long, IGuildMember>> fetchMembers(int limit, long after) {
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
	public CompletableFuture<Map<Long, IGuildMember>> fetchMembers(long after) {
		return loader.rest.loadGuildMembers(this, 50, after);
	}

	/**
	 * @return the afk_channel_id
	 */
	public IVoiceChannel getAfkChannel() {
		return voiceChannels.get(afk_channel_id);
	}

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
	public IGuildTextChannel getDefaultChannel() {
		return textChannels.get(id);
	}

	@Override
	public IRole getDefaultRole() {
		return getRoleByID(getID());
	}

	@Override
	public Map<Long, IGuildEmoji> getEmojis() {
		return emojis;
	}

	@Override
	public IIcon getIcon() {
		return new GuildIcon(this, icon);
	}

	public String getIconHash() {
		return icon;
	}

	public String getIconURL() {
		return iconURL;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return SnowflakeUtil.parse(id);
	}

	@Override
	public CompletableFuture<List<IIntegration>> getIntegrations() {
		return null;
	}

	/**
	 * Retrieves the guild's invites from Discord's API
	 * 
	 * @return A Future that completes with a HashMap of Invite objects, indexed
	 *         by {@link Invite#code}, if successful.
	 */
	public CompletableFuture<List<IInvite>> getInvites() {
		CompletableFuture<List<IInvite>> future = new CompletableFuture<>();
		List<IInvite> invites = new ArrayList<>();
		loader.rest.getInvites(this).thenAcceptAsync(action -> {
			for (InviteJSON data : action) {
				invites.add(EntityBuilder.getInviteFactory().buildInvite(data));
			}
			future.complete(invites);
		});
		return future;
	}

	@Override
	public DiscLoader getLoader() {
		return DiscLoader.getDiscLoader();
	}

	@Override
	public IGuildMember getMember(long memberID) {
		return members.get(memberID);
	}

	@Override
	public IGuildMember getMember(String memberID) {
		return getMember(SnowflakeUtil.parse(memberID));
	}

	@Override
	public int getMemberCount() {
		return memberCount;
	}

	@Override
	public Map<Long, IGuildMember> getMembers() {
		return members;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns a {@link GuildMember} object repersenting the guild's owner
	 * 
	 * @return A {@link GuildMember} object
	 */
	public IGuildMember getOwner() {
		return getMember(ownerID);
	}

	@Override
	public long getOwnerID() {
		return ownerID;
	}

	@Override
	public IPresence getPresence(long memberID) {
		return presences.get(memberID);
	}

	@Override
	public Map<Long, IPresence> getPresences() {
		return presences;
	}

	public CompletableFuture<Integer> getPruneCount() {
		return getPruneCount(1);
	}

	public CompletableFuture<Integer> getPruneCount(int days) {
		return loader.rest.pruneCount(this, days);
	}

	@Override
	public IRole getRoleByID(long roleID) {
		return roles.get(roleID);
	}

	@Override
	public Map<Long, IRole> getRoles() {
		return roles;
	}

	@Override
	public IGuildTextChannel getTextChannelByID(long channelID) {
		return textChannels.get(channelID);
	}

	@Override
	public IGuildTextChannel getTextChannelByID(String channelID) {
		return getTextChannelByID(SnowflakeUtil.parse(channelID));
	}

	@Override
	public IGuildTextChannel getTextChannelByName(String channelName) {
		for (IGuildTextChannel channel : textChannels.values())
			if (channel.getName().equals(channelName)) return channel;
		return null;
	}

	@Override
	public Map<Long, IGuildTextChannel> getTextChannels() {
		return textChannels;
	}

	@Override
	public IGuildVoiceChannel getVoiceChannelByID(long channelID) {
		return voiceChannels.get(channelID);
	}

	@Override
	public IGuildVoiceChannel getVoiceChannelByID(String channelID) {
		return getVoiceChannelByID(SnowflakeUtil.parse(channelID));
	}

	@Override
	public IGuildVoiceChannel getVoiceChannelByName(String channelName) {
		for (IGuildVoiceChannel channel : voiceChannels.values())
			if (channel.getName().equals(channelName)) return channel;
		return null;
	}

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

	@Override
	public CompletableFuture<List<VoiceRegion>> getVoiceRegions() {
		return new CompletableFuture<>();
	}

	@Override
	public Map<Long, VoiceState> getVoiceStates() {
		return rawStates;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean hasPermission(Permissions permissions) {
		return isOwner() || getCurrentMember().getPermissions().hasPermission(permissions);
	}

	@Override
	public boolean isAvailable() {
		return available;
	}

	public boolean isLarge() {
		return memberCount >= 250;
	}

	@Override
	public boolean isOwner() {
		return isOwner(getCurrentMember());
	}

	@Override
	public boolean isOwner(IGuildMember iGuildMember) {
		return getOwner().getID() == iGuildMember.getID();
	}

	@Override
	public boolean isSyncing() {
		return loader.isGuildSyncing(this);
	}

	public CompletableFuture<IGuildMember> kickMember(IGuildMember guildMember) {
		if (!isOwner() && getCurrentMember().getPermissions().hasPermission(Permissions.KICK_MEMBERS)) throw new PermissionsException();

		return loader.rest.removeMember(this, guildMember);
	}

	@Override
	public CompletableFuture<IGuild> leave() {
		CompletableFuture<IGuild> future = new CompletableFuture<>();
		this.kickMember(getCurrentMember()).thenAcceptAsync(action -> {
			future.complete(this);
		});

		return future;
	}

	@Override
	public IGuildMember removeMember(IGuildMember member) {
		members.remove(member.getID());
		memberCount--;
		return member;
	}

	@Override
	public void removeMember(IUser user) {
		members.remove(user.getID());
		memberCount--;
	}

	@Override
	public IRole removeRole(IRole role) {
		return roles.remove(role.getID());
	}

	@Override
	public IRole removeRole(long roleID) {
		return roles.remove(roleID);
	}

	@Override
	public IRole removeRole(String roleID) {
		return removeRole(SnowflakeUtil.parse(roleID));
	}

	public CompletableFuture<IGuild> setAFKChannel(IGuildVoiceChannel channel) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		if (!id.equals(channel.getGuild().getID())) throw new MissmatchException("Afk Channel cannot be set to a voice channel from another guild");
		return new ModifyGuild(this, new JSONObject().put("afk_channel_id", channel.getID())).execute();
	}

	public CompletableFuture<IGuild> setIcon(String icon) throws IOException {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		String base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(icon))));
		return new ModifyGuild(this, new JSONObject().put("icon", base64)).execute();
	}

	public CompletableFuture<IGuild> setName(String name) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		return new ModifyGuild(this, new JSONObject().put("name", name)).execute();
	}

	public CompletableFuture<IGuild> setOwner(IGuildMember member) {
		if (!isOwner()) throw new UnauthorizedException("Only the guild's owner can delete a guild");
		return new ModifyGuild(this, new JSONObject().put("owner_id", SnowflakeUtil.asString(member))).execute();
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
			ownerID = SnowflakeUtil.parse(data.owner_id);
			memberCount = data.member_count;
			voiceRegion = new VoiceRegion(data.region);
			splashHash = data.splash;
			// ProgressLogger.step(1, 7, "Caching Roles");
			if (data.roles.length > 0) {
				roles.clear();
				for (RoleJSON role : data.roles) {
					IRole r = gfac.buildRole(this, role);
					roles.put(r.getID(), r);
				}
			}
			// ProgressLogger.step(2, 7, "Caching Members");
			if (data.members != null && data.members.length > 0) {
				members.clear();
				for (MemberJSON member : data.members) {
					IGuildMember m = gfac.buildMember(this, member);
					members.put(m.getID(), m);
				}
			}
			// ProgressLogger.step(3, 7, "Caching Channels");
			if (data.channels != null && data.channels.length > 0) {
				for (ChannelJSON channelData : data.channels) {
					IGuildChannel chan = (IGuildChannel) EntityRegistry.addChannel(channelData, this);
					if (chan instanceof IGuildTextChannel) textChannels.put(chan.getID(), (IGuildTextChannel) chan);
					else if (chan instanceof IGuildVoiceChannel) voiceChannels.put(chan.getID(), (IGuildVoiceChannel) chan);
				}
			}
			// ProgressLogger.step(4, 7, "Caching Presences");
			if (data.presences != null && data.presences.length > 0) {
				presences.clear();
				for (PresenceJSON presence : data.presences) {
					this.setPresence(presence);
				}
			}
			// ProgressLogger.step(5, 7, "Caching Emojis");
			if (data.emojis != null && data.emojis.length > 0) {
				this.emojis.clear();
				for (EmojiJSON e : data.emojis) {
					this.emojis.put(SnowflakeUtil.parse(e.id), new Emoji(e, this));
				}
			}
			// ProgressLogger.step(6, 7, "Caching Voice States");
			if (data.voice_states != null && data.voice_states.length > 0) {
				this.rawStates.clear();
				for (VoiceStateJSON v : data.voice_states) {
					this.rawStates.put(SnowflakeUtil.parse(v.user_id), new VoiceState(v, this));
				}
			}
			this.available = data.unavailable == true ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CompletableFuture<IGuild> setVoiceRegion(String region) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD)) throw new PermissionsException("Insuficient Permissions");
		return new ModifyGuild(this, new JSONObject().put("region", region)).execute();
	}

	public void sync() throws GuildSyncException, AccountTypeException {
		loader.syncGuilds(this.id);
	}

	@Override
	public void updateVoiceState(VoiceState state) {
		rawStates.put(state.member.getID(), state);
	}

}