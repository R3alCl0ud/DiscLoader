package io.discloader.discloader.entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.GuildMemberAddEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.entity.channels.Channel;
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.EmojiJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.json.VoiceStateJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * This represents a Guild in Discord's API
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class Guild {

	/**
	 * The guild's Snowflake ID.
	 */
	public final String id;

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
	 * The amount of members in the guild. Does not reflect this.members.size()
	 */
	public int memberCount;

	/**
	 * Whether or not the guild is currently available
	 */
	public boolean available;

	/**
	 * Whether or not the guild has more than 250 members
	 */
	public boolean large;

	/**
	 * A GuildMember object repersenting the guild's owner
	 * 
	 * @see GuildMember
	 */
	public GuildMember owner;

	/**
	 * The instance of the {@link DiscLoader loader} the cached the guild
	 */
	public final DiscLoader loader;

	/**
	 * A HashMap of the guild's cached members. Indexed by member ID.
	 * 
	 * @see GuildMember
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, GuildMember> members;

	/**
	 * A HashMap of the guild's TextChannels. Indexed by channel ID.
	 * 
	 * @see TextChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, TextChannel> textChannels;

	/**
	 * A HashMap of the guild's VoiceChannels. Indexed by channel ID.
	 * 
	 * @see VoiceChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, VoiceChannel> voiceChannels;

	/**
	 * A HashMap of the guild's roles. Indexed by role ID.
	 * 
	 * @see Role
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, Role> roles;

	/**
	 * A HashMap of the presences of the guild's members
	 * 
	 * @see GuildMember
	 * @see Presence
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, Presence> presences;

	/**
	 * A HashMap of the guild's custom emojis. Indexed by {@link Emoji#id}
	 * 
	 * @author Perry Berman
	 */
	public HashMap<String, Emoji> emojis;

	/**
	 * A Private HashMap of the guild's raw voice states. Indexed by
	 * {@link GuildMember#id}
	 * 
	 * @author Perry Berman
	 */
	private HashMap<String, VoiceState> rawStates;

	/**
	 * The guild's current voice region
	 */
	public VoiceRegion voiceRegion;

	/**
	 * Creates a new guild
	 * 
	 * @param loader
	 * @param data The guild's data
	 */
	public Guild(DiscLoader loader, GuildJSON data) {
		this.loader = loader;

		this.members = new HashMap<String, GuildMember>();
		this.textChannels = new HashMap<String, TextChannel>();
		this.voiceChannels = new HashMap<String, VoiceChannel>();
		this.roles = new HashMap<String, Role>();
		this.presences = new HashMap<String, Presence>();
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

	public GuildMember addMember(MemberJSON data) {
		return this.addMember(data, false);
	}

	public GuildMember addMember(MemberJSON data, boolean shouldEmit) {
		boolean exists = this.members.containsKey(data.user.id);
		GuildMember member = new GuildMember(this, data);
		this.members.put(member.id, member);
		if (member.id.equals(this.ownerID))
			this.owner = member;
		if (!exists && this.loader.ready && shouldEmit) {
			GuildMemberAddEvent event = new GuildMemberAddEvent(member);
			this.loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
			for (IEventListener e : DiscLoader.handlers.values()) {
				e.GuildMemberAdd(event);
			}
		}
		return member;
	}

	public GuildMember addMember(User user, String[] roles, boolean deaf, boolean mute, String nick,
			boolean emitEvent) {
		boolean exists = this.members.containsKey(user.id);
		GuildMember member = new GuildMember(this, user, roles, deaf, mute, nick);
		this.members.put(member.id, member);
		if (member.id.equals(this.ownerID))
			this.owner = member;
		if (this.loader.ready == true && emitEvent && !exists) {
			GuildMemberAddEvent event = new GuildMemberAddEvent(member);
			this.loader.emit(DLUtil.Events.GUILD_MEMBER_ADD, event);
			for (IEventListener e : DiscLoader.handlers.values()) {
				e.GuildMemberAdd(event);
			}
		}

		return member;
	}

	public Role addRole(RoleJSON guildRole) {
		boolean exists = this.roles.containsKey(guildRole.id);
		Role role = new Role(this, guildRole);
		this.roles.put(role.id, role);
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
	public CompletableFuture<GuildMember> ban(GuildMember member) {
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
			base64 = new String(
					"data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(image.toPath())));
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
		return null;
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
		return null;
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
	public CompletableFuture<Role> createRole(String name, int permissions, int color, boolean hoist,
			boolean mentionable) {
		return null;
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
	 * @param name
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
		return this.loader.rest.createVoiceChannel(this,
				new JSONObject().put("name", name).put("bitrate", bitrate).put("user_limit", userLimit));
	}

	/**
	 * Deletes the Guild if the user you have logged in as is the owner of the
	 * guild
	 * 
	 * @return A Future that completes with {@code this} if successful, and
	 *         fails with a {@link UnauthorizedException}
	 */
	public CompletableFuture<Guild> delete() {
		CompletableFuture<Guild> future = new CompletableFuture<Guild>();
		this.loader.rest.makeRequest(DLUtil.Endpoints.guild(this.id), DLUtil.Methods.DELETE, true)
				.thenAcceptAsync(data -> {
					future.complete(this);
				});
		return future;
	}

	/**
	 * Fetches a GuildMember from the REST API
	 * 
	 * @param memberID the ID of the member to load
	 * @return CompletableFuture.GuildMember
	 */
	public CompletableFuture<GuildMember> fetchMember(String memberID) {
		return this.loader.rest.loadGuildMember(this, memberID);
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild.
	 * 
	 * @param limit max number of members to return (1-1000) default 50
	 * @param after The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers
	 *         if successful, null otherwise.
	 */
	public CompletableFuture<HashMap<String, GuildMember>> fetchMembers(int limit, String after) {
		return this.loader.rest.loadGuildMembers(this, limit, after);
	}

	/**
	 * Gets a HashMap of GuildMembers that are in the guild. <u>Only retrieves
	 * 50 members</u>
	 * 
	 * @param after The highest user id in the previous page
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers
	 *         if successful, null otherwise.
	 */
	public CompletableFuture<HashMap<String, GuildMember>> fetchMembers(String after) {
		return this.loader.rest.loadGuildMembers(this, 50, after);
	}

	/**
	 * Gets the guild's default text channel. the {@link Channel#id id} of the
	 * channel should be the same as the guild's {@link #id}
	 * 
	 * @return the default TextChannel
	 */
	public TextChannel getDefaultChannel() {
		return this.textChannels.get(this.id);
	}

	public CompletableFuture<HashMap<String, Invite>> getInvites() {
		return null;
	}

	/**
	 * same as {@link #getPruneCount(int)} but only grabs one days worth
	 * 
	 * @return A Future that completes with the number of member that would be
	 *         kicked in a prune operation
	 */
	public CompletableFuture<Integer> getPruneCount() {
		return getPruneCount(1);
	}

	/**
	 * Returns the number of members that would be removed in a prune operation.
	 * Requires the {@literal KICK_MEMBERS} permission.
	 * 
	 * @param count The number of days to count prune for (1 or more)
	 * @return A Future that completes with the number of member that would be
	 *         kicked in a prune operation
	 */
	public CompletableFuture<Integer> getPruneCount(int count) {
		return this.loader.rest.pruneCount(this, count);
	}

	/**
	 * Kicks the member from the {@link Guild} if the {@link DiscLoader client}
	 * has sufficient permissions
	 * 
	 * @param member The member to kick from the guild
	 * @see Permission
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 */
	public CompletableFuture<GuildMember> kickMember(GuildMember member) {
		return this.loader.rest.removeMember(this, member);
	}

	/**
	 * Sets the guild's icon if the loader has sufficient permissions
	 * 
	 * @param icon location of icon file on disk
	 * @return CompletableFuture
	 * @throws IOException
	 */
	public CompletableFuture<Guild> setIcon(String icon) throws IOException {
		String base64 = new String(
				"data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(icon))));
		return this.loader.rest.modifyGuild(this, new JSONObject().put("icon", base64));
	}

	/**
	 * Sets the guild's name if the loader has sufficient permissions
	 * 
	 * @param name The guild's new name
	 * @return CompletableFuture
	 */
	public CompletableFuture<Guild> setName(String name) {
		return this.loader.rest.modifyGuild(this, new JSONObject().put("name", name));
	}

	public void setPresence(PresenceJSON guildPresence) {
		if (this.presences.containsKey(guildPresence.user.id)) {
			this.presences.get(guildPresence.user.id).update(guildPresence);
			return;
		}
		Presence presence = new Presence(guildPresence);
		if (guildPresence.user.id.equals(this.loader.user.id))
			this.loader.user.presence.update(guildPresence);
		this.presences.put(guildPresence.user.id, presence);
	}

	/**
	 * Sets up a guild with data from the gateway
	 * 
	 * @param data The guild's data
	 */
	public void setup(GuildJSON data) {

		this.name = data.name;
		this.icon = data.icon != null ? data.icon : null;
		this.iconURL = this.icon != null ? DLUtil.Endpoints.guildIcon(this.id, this.icon) : null;
		this.ownerID = data.owner_id;
		this.memberCount = data.member_count;
		this.voiceRegion = new VoiceRegion(data.region);
		this.large = data.large;
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
			this.textChannels.clear();
			this.voiceChannels.clear();
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
				this.emojis.put(e.id, new Emoji(e, this));
			}
		}
		ProgressLogger.step(6, 7, "Caching Voice States");
		if (data.voice_states != null && data.voice_states.length > 0) {
			this.rawStates.clear();
			for (VoiceStateJSON v : data.voice_states) {
				this.rawStates.put(v.user_id, new VoiceState(v, this));
			}
		}

		ProgressLogger.step(7, 7, "Registering Icon");
		try {
			TextureRegistry.registerGuildIcon(new GuildIcon(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.available = !data.unavailable;
	}

	/**
	 * Sets the Guild's voice region to the specified region
	 * 
	 * @param region The new voice region
	 * @return {@link CompletableFuture}
	 */
	public CompletableFuture<Guild> setVoiceRegion(String region) {
		return this.loader.rest.modifyGuild(this, new JSONObject().put("region", region));
	}

	/**
	 * Syncs the guild to the client if the logged in user is not a bot
	 */
	public void sync() {
		this.loader.syncGuilds(this.id);
	}

}