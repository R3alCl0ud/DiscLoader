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
import org.json.JSONArray;
import org.json.JSONObject;

import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.client.render.texture.icon.GuildSplash;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.exceptions.MissmatchException;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.core.entity.Presence;
import io.discloader.discloader.core.entity.auditlog.AuditLog;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.invite.Invite;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.auditlog.ActionTypes;
import io.discloader.discloader.entity.auditlog.IAuditLog;
import io.discloader.discloader.entity.auditlog.IAuditLogEntry;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
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
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.AuditLogJSON;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.EmojiJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.json.VoiceRegionJSON;
import io.discloader.discloader.network.json.VoiceStateJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.guild.CreateRole;
import io.discloader.discloader.network.rest.actions.guild.ModifyGuild;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;
import io.discloader.discloader.util.DLUtil;

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

	private String id, name, icon, iconURL, splashHash, afk_channel_id;

	public long ownerID;

	public GuildSplash splash;

	private int memberCount;

	public boolean available;

	private final DiscLoader loader;

	public Map<Long, IGuildMember> members;
	private Map<Long, IGuildTextChannel> textChannels;
	private Map<Long, IGuildVoiceChannel> voiceChannels;
	private Map<Long, IChannelCategory> categories;
	public Map<Long, IRole> roles;
	public Map<Long, IPresence> presences;
	public Map<Long, IGuildEmoji> guildEmojis;
	private Map<Long, VoiceState> rawStates;
	private Map<String, IInvite> invites;

	/**
	 * The guild's current voice region
	 */
	private VoiceRegion voiceRegion;

	private GuildFactory gfac = EntityBuilder.getGuildFactory();

	/**
	 * Creates a new guild
	 * 
	 * @param loader
	 *            The current instance of DiscLoader
	 * @param data
	 *            The guild's data
	 */
	public Guild(DiscLoader loader, GuildJSON data) {
		this.loader = loader;

		members = new HashMap<>();
		textChannels = new HashMap<>();
		voiceChannels = new HashMap<>();
		categories = new HashMap<>();
		roles = new HashMap<>();
		presences = new HashMap<>();
		guildEmojis = new HashMap<>();
		rawStates = new HashMap<>();
		invites = new HashMap<>();
		voiceRegion = new VoiceRegion("us-central");

		if (data != null && data.unavailable == true) {
			available = false;
			id = data.id;
		} else if (data != null) {
			available = true;
			id = data.id;
			setup(data);
		} else {
			available = false;
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
	 * Method used internally by DiscLoader to make a new {@link GuildMember} object
	 * when a member's data is recieved
	 * 
	 * @param user
	 *            the member's {@link User} object.
	 * @param roles
	 *            the member's role's ids.
	 * @param deaf
	 *            is the member deafened.
	 * @param mute
	 *            is the member muted.
	 * @param nick
	 *            The member's nickname.
	 * @param emitEvent
	 *            if a {@code GuildMemberAddEvent} should be fired by the client.
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
			// loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
			loader.emit(event);
		}

		return member;
	}

	/**
	 * Method used internally by DiscLoader to make a new {@link GuildMember} object
	 * when a member's data is recieved
	 * 
	 * @param data
	 *            The member's data
	 * @return The {@link GuildMember} that was instantiated.
	 */
	@Override
	public IGuildMember addMember(MemberJSON data) {
		return this.addMember(data, false);
	}

	/**
	 * Method used internally by DiscLoader to make a new {@link GuildMember} object
	 * when a member's data is recieved
	 * 
	 * @param data
	 *            The member's data
	 * @param shouldEmit
	 *            if a {@code GuildMemberAddEvent} should be fired by the client
	 * @return The {@link GuildMember} that was instantiated.
	 */
	public IGuildMember addMember(MemberJSON data, boolean shouldEmit) {
		boolean exists = members.containsKey(SnowflakeUtil.parse(data.user.id));
		IGuildMember member = new GuildMember(this, data);
		members.put(member.getID(), member);

		if (!exists && shouldEmit) {
			memberCount++;
			GuildMemberAddEvent event = new GuildMemberAddEvent(member);
			// loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
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
	public IRole addRole(RoleJSON guildRole) {
		boolean exists = roles.containsKey(SnowflakeUtil.parse(guildRole.id));
		IRole role = new Role(this, guildRole);
		roles.put(role.getID(), role);
		GuildRoleCreateEvent event = new GuildRoleCreateEvent(role);
		if (!exists && this.loader.ready) {
			loader.emit(event);
		}
		return role;
	}

	@Override
	public CompletableFuture<IGuildMember> ban(IGuildMember member) {
		if (!hasPermission(Permissions.BAN_MEMBERS))
			throw new PermissionsException("");
		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		loader.rest.request(Methods.PUT, Endpoints.guildBanMember(getID(), member.getID()), new RESTOptions(), Void.class).thenAcceptAsync(action -> {
			future.complete(member);
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildMember> ban(IGuildMember member, String reason) throws PermissionsException {
		if (!hasPermission(Permissions.BAN_MEMBERS))
			throw new PermissionsException("");
		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		loader.rest.request(Methods.PUT, Endpoints.guildBanMember(getID(), member.getID()), new RESTOptions(reason), Void.class).thenAcceptAsync(action -> {
			future.complete(member);
		});
		return future;
	}

	@Override
	public CompletableFuture<Integer> beginPrune() {
		if (!getCurrentMember().getPermissions().hasPermission(Permissions.KICK_MEMBERS))
			throw new PermissionsException("Pruning members requires the 'KICK_MEMBERS' permission");
		return beginPrune(1);
	}

	@Override
	public CompletableFuture<Integer> beginPrune(int days) {
		if (!getCurrentMember().getPermissions().hasPermission(Permissions.KICK_MEMBERS))
			throw new PermissionsException("Pruning members requires the 'KICK_MEMBERS' permission");
		CompletableFuture<Integer> future = new CompletableFuture<>();
		loader.rest.request(Methods.POST, Endpoints.guildPrune(getID()), new RESTOptions(), Integer.class).thenAcceptAsync(pruned -> {
			future.complete(pruned);
		});
		return future;
	}

	public IGuild clone() {
		Guild guild = new Guild(loader, null);
		guildEmojis.forEach((id, emoji) -> guild.guildEmojis.put(id, emoji));
		roles.forEach((id, role) -> guild.addRole(role));
		members.forEach((id, member) -> guild.addMember(member));
		rawStates.forEach((id, state) -> guild.rawStates.put(id, state));
		textChannels.forEach((id, channel) -> guild.textChannels.put(id, channel));
		voiceChannels.forEach((id, channel) -> guild.voiceChannels.put(id, channel));
		presences.forEach((id, presence) -> guild.presences.put(id, presence));
		guild.available = available;
		guild.name = name;
		guild.icon = icon;
		guild.iconURL = iconURL;
		guild.id = id;
		guild.voiceRegion = voiceRegion;
		guild.memberCount = memberCount;
		guild.ownerID = ownerID;
		guild.splash = splash;
		guild.splashHash = splashHash;
		guild.afk_channel_id = afk_channel_id;
		return guild;
	}

	@Override
	public CompletableFuture<IChannelCategory> createCategory(String name) {
		CompletableFuture<IChannelCategory> future = new CompletableFuture<>();
		JSONObject chanSet = new JSONObject().put("name", name).put("type", 4);
		loader.rest.request(Methods.POST, Endpoints.guildChannels(getID()), new RESTOptions(chanSet), ChannelJSON.class).thenAcceptAsync(d -> {
			IChannel channel = EntityBuilder.getChannelFactory().buildChannel(d, loader, this, false);
			if (channel instanceof IChannelCategory)
				future.complete((IChannelCategory) channel);
		});

		return future;
	}

	@Override
	public CompletableFuture<IChannelCategory> createCategory(String name, IOverwrite... overwrites) {
		CompletableFuture<IChannelCategory> future = new CompletableFuture<>();
		JSONArray ows = new JSONArray();
		for (IOverwrite ow : overwrites) {
			ows.put(ow);
		}
		JSONObject chanSet = new JSONObject().put("name", name).put("type", 4).put("permission_overwrites", ows);
		loader.rest.request(Methods.POST, Endpoints.guildChannels(getID()), new RESTOptions(chanSet), ChannelJSON.class).thenAcceptAsync(d -> {
			IChannel channel = EntityBuilder.getChannelFactory().buildChannel(d, loader, this, false);
			if (channel instanceof IChannelCategory)
				future.complete((IChannelCategory) channel);
		});

		return future;
	}

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
	 * @return A future that completes with a new {@link Role} Object if successful.
	 * @since 0.0.3
	 */
	public CompletableFuture<IRole> createRole() {
		return createRole(null, 0, 0, false, false);
	}

	@Override
	public CompletableFuture<IRole> createRole(String name) {
		return new CreateRole(this, new SendableRole(name, 0, 0, false, false)).execute();
	}

	/**
	 * Creates a new {@link Role}.
	 * 
	 * @param name
	 *            The name of the role
	 * @param permissions
	 *            The 53bit Permissions integer to assign to the role
	 * @param color
	 *            The color of the role
	 * @return A future that completes with a new {@link Role} Object if successful.
	 * @since 0.0.3
	 */
	public CompletableFuture<IRole> createRole(String name, long permissions, int color) {
		return this.createRole(name, permissions, color, false, false);
	}

	public CompletableFuture<IRole> createRole(String name, long permissions, int color, boolean hoist, boolean mentionable) {
		return new CreateRole(this, new SendableRole(name, permissions, color, hoist, mentionable)).execute();
	}

	public CompletableFuture<IGuildTextChannel> createTextChannel(String name) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> createTextChannel(String name, IOverwrite... overwrites) {
		return null;
	}

	/**
	 * Creates a new {@link VoiceChannel}
	 * 
	 * @param name
	 *            The channel's name
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
	 * @param name
	 *            The name of the channel
	 * @param bitrate
	 *            The channel's bitrate
	 * @return A future that completes with a new {@link VoiceChannel} Object if
	 *         successful.
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name, int bitrate) {
		return this.loader.rest.createVoiceChannel(this, new JSONObject().put("name", name).put("bitrate", bitrate));
	}

	/**
	 * Creates a new {@link VoiceChannel}
	 * 
	 * @param name
	 *            The name of the channel
	 * @param bitrate
	 *            The channel's bitrate
	 * @param userLimit
	 *            the channel's userlimit
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
		if (!isOwner())
			throw new UnauthorizedException("Only the guild's owner can delete a guild");
		CompletableFuture<IGuild> future = new CompletableFuture<>();
		loader.rest.makeRequest(Endpoints.guild(getID()), DLUtil.Methods.DELETE, true).thenAcceptAsync(data -> {
			future.complete(this);
		});
		return future;
	}

	public CompletableFuture<IGuild> edit(String name, String icon, IGuildVoiceChannel afkChannel) throws IOException {
		if (!isOwner() && getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD))
			throw new PermissionsException();
		return new CompletableFuture<>();
	}

	/**
	 * @return {@code true} if all fields are equivalent, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Guild))
			return false;
		Guild guild = (Guild) object;

		return this == guild || getID() == guild.getID();
	}

	@Override
	public CompletableFuture<List<IInvite>> fetchInvites() {
		return null;
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
		final Consumer<Object> consumer = event -> {
			if (event instanceof GuildMembersChunkEvent) {
				GuildMembersChunkEvent gmce = (GuildMembersChunkEvent) event;
				future.complete(gmce.members);
			}
		};
		loader.onceEvent(consumer, guild -> guild.getID() == getID());
		Packet payload = new Packet(8, new MemberQuery(limit, ""));
		loader.socket.send(payload);
		return future;
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild.
	 * 
	 * @param limit
	 *            max number of members to return (1-1000) default 50
	 * @param after
	 *            The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers if
	 *         successful, null otherwise.
	 */
	public CompletableFuture<Map<Long, IGuildMember>> fetchMembers(int limit, long after) {
		return loader.rest.loadGuildMembers(this, limit, after);
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild. <u>Only retrieves 50
	 * members</u>
	 * 
	 * @param after
	 *            The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers if
	 *         successful, null otherwise.
	 */
	public CompletableFuture<Map<Long, IGuildMember>> fetchMembers(long after) {
		return loader.rest.loadGuildMembers(this, 50, after);
	}

	/**
	 * @return the afk_channel_id
	 */
	public IGuildVoiceChannel getAfkChannel() {
		return getVoiceChannelByID(afk_channel_id);
	}

	@Override
	public CompletableFuture<IAuditLog> getAuditLog(ActionTypes action) {
		CompletableFuture<IAuditLog> future = new CompletableFuture<>();
		return future;
	}

	@Override
	public CompletableFuture<IAuditLog> getAuditLog(IAuditLogEntry before) {
		CompletableFuture<IAuditLog> future = new CompletableFuture<>();
		return future;
	}

	@Override
	public CompletableFuture<IAuditLog> getAuditLog(IUser user) {
		CompletableFuture<IAuditLog> future = new CompletableFuture<>();
		return future;
	}

	@Override
	public CompletableFuture<IAuditLog> getAuditLog(IUser user, ActionTypes action, IAuditLogEntry before, short limit) {
		CompletableFuture<IAuditLog> future = new CompletableFuture<>();
		JSONObject params = new JSONObject().put("user_id", SnowflakeUtil.asString(user)).put("action_type", action.toInt()).put("before", SnowflakeUtil.asString(before)).put("limit", limit);
		loader.rest.request(Methods.GET, Endpoints.auditLogs(getID()), new RESTOptions(params), AuditLogJSON.class).thenAcceptAsync(al -> {
			future.complete(new AuditLog(this, al));
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IAuditLog> getAuditLog(short limit) {
		return null;
	}

	@Override
	public Map<Long, IChannelCategory> getChannelCategories() {
		return categories;
	}

	@Override
	public IChannelCategory getChannelCategoryByID(long id) {
		return null;
	}

	@Override
	public IChannelCategory getChannelCategoryByID(String id) {
		return null;
	}

	@Override
	public IChannelCategory getChannelCategoryByName(String name) {
		return null;
	}

	@Override
	public Map<Long, IGuildChannel> getChannels() {
		Map<Long, IGuildChannel> channels = new HashMap<>();
		for (IGuildTextChannel channel : textChannels.values()) {
			channels.put(channel.getID(), channel);
		}
		for (IGuildVoiceChannel channel : voiceChannels.values()) {
			channels.put(channel.getID(), channel);
		}
		return channels;
	}

	@Override
	public IGuildMember getCurrentMember() {
		return members.get(loader.user.getID());
	}

	/**
	 * Gets the guild's default text channel. The "default" channel for a given user
	 * is now the channel with the highest position that their {@link Permissions}
	 * allow them to see.
	 * 
	 * @return the default TextChannel
	 */
	public IGuildTextChannel getDefaultChannel() {
		IGuildTextChannel defaultChannel = null;
		for (IGuildTextChannel channel : textChannels.values()) {
			if ((defaultChannel == null || channel.getPosition() < defaultChannel.getPosition()) && channel.permissionsOf(getCurrentMember()).hasPermission(Permissions.READ_MESSAGES, true)) {
				defaultChannel = channel;
			}
		}
		return defaultChannel;
	}

	@Override
	public IRole getDefaultRole() {
		return getRoleByID(getID());
	}

	@Override
	public Map<Long, IGuildEmoji> getEmojis() {
		return guildEmojis;
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

	@Override
	public IInvite getInvite(String code) {
		return null;
	}

	/**
	 * Retrieves the guild's invites from Discord's API
	 * 
	 * @return A Future that completes with a HashMap of Invite objects, indexed by
	 *         {@link Invite#code}, if successful.
	 */
	@Override
	public List<IInvite> getInvites() {
		return new ArrayList<>(invites.values());
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
	public IRole getRoleByID(String roleID) {
		return getRoleByID(SnowflakeUtil.parse(roleID));
	}

	@Override
	public IRole getRoleByName(String name) {
		for (IRole role : roles.values()) {
			if (role.getName().equalsIgnoreCase(name))
				return role;
		}
		return null;
	}

	@Override
	public Map<Long, IRole> getRoles() {
		return roles;
	}

	@Override
	public IIcon getSplash() {
		return splash;
	}

	@Override
	public String getSplashHash() {
		return splashHash;
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
			if (channel.getName().equals(channelName))
				return channel;
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
			if (channel.getName().equals(channelName))
				return channel;
		return null;
	}

	@Override
	public Map<Long, IGuildVoiceChannel> getVoiceChannels() {
		return voiceChannels;
	}

	@Override
	public VoiceConnection getVoiceConnection() {
		return EntityRegistry.getVoiceConnectionByGuild(this);
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
		CompletableFuture<List<VoiceRegion>> future = new CompletableFuture<List<VoiceRegion>>();
		CompletableFuture<VoiceRegionJSON[]> cf = getLoader().rest.request(Methods.GET, Endpoints.guildRegions(getID()), new RESTOptions(), VoiceRegionJSON[].class);
		cf.thenAcceptAsync(regions -> {
			List<VoiceRegion> rgs = new ArrayList<>();
			for (VoiceRegionJSON region : regions) {
				rgs.add(new VoiceRegion(region));
			}
			
			future.complete(rgs);
		});
		return future;
	}

	@Override
	public Map<Long, VoiceState> getVoiceStates() {
		return rawStates;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}

	@Override
	public boolean hasPermission(Permissions... permissions) {
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

	public CompletableFuture<IGuildMember> kick(IGuildMember guildMember) {
		return kick(guildMember, null);
	}

	@Override
	public CompletableFuture<IGuildMember> kick(IGuildMember member, String reason) {
		if (!isOwner() && getCurrentMember().getPermissions().hasPermission(Permissions.KICK_MEMBERS))
			throw new PermissionsException("Insufficient Permissions");

		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		CompletableFuture<Void> kickFuture = loader.rest.request(Methods.DELETE, Endpoints.guildMember(getID(), member.getID()), new RESTOptions(reason), Void.class);

		kickFuture.thenAcceptAsync(n -> {
			future.complete(member);
		});

		kickFuture.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});

		return future;
	}

	@Override
	public CompletableFuture<IGuild> leave() {
		CompletableFuture<IGuild> future = new CompletableFuture<>();
		this.kick(getCurrentMember()).thenAcceptAsync(action -> {
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
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD))
			throw new PermissionsException("Insuficient Permissions");
		if (!id.equals(channel.getGuild().getID()))
			throw new MissmatchException("Afk Channel cannot be set to a voice channel from another guild");
		return new ModifyGuild(this, new JSONObject().put("afk_channel_id", channel.getID())).execute();
	}

	public CompletableFuture<IGuild> setIcon(String icon) throws IOException {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD))
			throw new PermissionsException("Insuficient Permissions");
		String base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(icon))));
		return new ModifyGuild(this, new JSONObject().put("icon", base64)).execute();
	}

	public CompletableFuture<IGuild> setName(String name) {
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD))
			throw new PermissionsException("Insuficient Permissions");
		return new ModifyGuild(this, new JSONObject().put("name", name)).execute();
	}

	public CompletableFuture<IGuild> setOwner(IGuildMember member) {
		if (!isOwner())
			throw new UnauthorizedException("Only the guild's owner can delete a guild");
		return new ModifyGuild(this, new JSONObject().put("owner_id", SnowflakeUtil.asString(member))).execute();
	}

	@Override
	public void setPresence(PresenceJSON guildPresence) {
		setPresence(guildPresence, false);
	}

	public void setPresence(PresenceJSON guildPresence, boolean shouldEmit) {
		IPresence presence = new Presence(guildPresence);
		if (guildPresence.user.id == null) {
			System.out.println("user is null");
			return;
		}
		if (guildPresence.user.id.equals(this.loader.user.getID())) {
			System.out.println(DLUtil.gson.toJson(guildPresence));
			loader.user.getPresence().update(guildPresence);
		}
		presences.put(SnowflakeUtil.parse(guildPresence.user.id), presence);
	}

	/**
	 * Sets up a guild with data from the gateway
	 * 
	 * @param data
	 *            The guild's data
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
			if (data.roles.length > 0) {
				roles.clear();
				for (RoleJSON role : data.roles) {
					IRole r = gfac.buildRole(this, role);
					roles.put(r.getID(), r);
				}
			}
			if (data.members != null && data.members.length > 0) {
				members.clear();
				for (MemberJSON member : data.members) {
					IGuildMember m = gfac.buildMember(this, member);
					members.put(m.getID(), m);
				}
			}
			if (data.channels != null && data.channels.length > 0) {
				for (ChannelJSON channelData : data.channels) {
					IGuildChannel chan = (IGuildChannel) EntityRegistry.addChannel(channelData, getLoader(), this);
					if (chan instanceof IGuildTextChannel)
						textChannels.put(chan.getID(), (IGuildTextChannel) chan);
					else if (chan instanceof IGuildVoiceChannel)
						voiceChannels.put(chan.getID(), (IGuildVoiceChannel) chan);
				}
			}
			if (data.presences != null && data.presences.length > 0) {
				presences.clear();
				for (PresenceJSON presence : data.presences) {
					this.setPresence(presence);
				}
			}
			if (data.emojis != null && data.emojis.length > 0) {
				this.guildEmojis.clear();
				for (EmojiJSON e : data.emojis) {
					this.guildEmojis.put(SnowflakeUtil.parse(e.id), new GuildEmoji(e, this));
				}
			}
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
		if (!isOwner() && !getCurrentMember().getPermissions().hasPermission(Permissions.MANAGE_GUILD))
			throw new PermissionsException("Insuficient Permissions");
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