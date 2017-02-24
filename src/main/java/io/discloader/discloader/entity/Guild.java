package io.discloader.discloader.entity;

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
import io.discloader.discloader.entity.channels.TextChannel;
import io.discloader.discloader.entity.channels.VoiceChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.util.Constants;

/**
 * This represents a Guild in Discord's API
 * @author Perry Berman
 * @since 0.0.1_Alpha
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
	 * @see GuildMember
	 */
	public GuildMember owner;

	/**
	 * The instance of the {@link DiscLoader loader} the cached the guild
	 */
	public final DiscLoader loader;

	/**
	 * A HashMap of the guild's cached members. Indexed by member ID.
	 * @see GuildMember
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, GuildMember> members;

	/**
	 * A HashMap of the guild's TextChannels. Indexed by channel ID.
	 * @see TextChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, TextChannel> textChannels;

	/**
	 * A HashMap of the guild's VoiceChannels. Indexed by channel ID.
	 * @see VoiceChannel
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, VoiceChannel> voiceChannels;

	/**
	 * A HashMap of the guild's roles. Indexed by role ID.
	 * @see Role
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, Role> roles;

	/**
	 * A HashMap of the presences of the guild's members
	 * @see GuildMember
	 * @see Presence
	 * @see HashMap
	 * @author Perry Berman
	 */
	public HashMap<String, Presence> presences;

	public Guild(DiscLoader loader, GuildJSON data) {
		this.loader = loader;

		this.members = new HashMap<String, GuildMember>();
		this.textChannels = new HashMap<String, TextChannel>();
		this.voiceChannels = new HashMap<String, VoiceChannel>();
		this.roles = new HashMap<String, Role>();
		this.presences = new HashMap<String, Presence>();

		if (data.unavailable == true) {
			this.available = false;
			this.id = data.id;
		} else {
			this.available = true;
			this.id = data.id;
			this.setup(data);
		}
	}

	public void setup(GuildJSON data) {

		this.name = data.name;
		this.icon = data.icon != null ? data.icon : null;
		this.iconURL = this.icon != null ? Constants.Endpoints.guildIcon(this.id, this.icon) : null;
		this.ownerID = data.owner_id;
		this.memberCount = data.member_count;
		this.large = data.large;
		ProgressLogger.step(1, 5, "Caching Roles");
		if (data.roles.length > 0) {
			this.roles.clear();
			for (RoleJSON role : data.roles) {
				this.addRole(role);
//				ProgressLogger.progress(this.roles.size(), data.roles.length, String.format("Cached Role: %s", role.id));
			}
		}
		ProgressLogger.step(2, 5, "Caching Members");
		if (data.members != null && data.members.length > 0) {
			this.members.clear();
			for (MemberJSON member : data.members) {
				this.addMember(member);
			}
		}
		ProgressLogger.step(3, 5, "Caching Channels");
		if (data.channels != null && data.channels.length > 0) {
			this.textChannels.clear();
			this.voiceChannels.clear();
			for (ChannelJSON channel : data.channels) {
				this.loader.addChannel(channel, this);
//				ProgressLogger.progress(this.textChannels.size() + this.voiceChannels.size(), data.channels.length, String.format("Cached Channel: %s", channel.id));
			}
		}
		ProgressLogger.step(4, 5, "Caching Presences");
		if (data.presences != null && data.presences.length > 0) {
			this.presences.clear();
			for (PresenceJSON presence : data.presences) {
				this.setPresence(presence);
//				ProgressLogger.progress(this.presences.size(), data.presences.length, String.format("Cached Presence: %s", presence.user.id));
			}
		}
		ProgressLogger.step(5, 5, "Registering Icon");
//		ProgressLogger.progress(1, 1, String.format("IconURL: %s", this.iconURL));
		TextureRegistry.registerGuildIcon(new GuildIcon(this));
		this.available = !data.unavailable;
	}

	public GuildMember addMember(MemberJSON data) {
		boolean exists = this.members.containsKey(data.user.id);
		GuildMember member = new GuildMember(this, data);
		this.members.put(member.id, member);
		if (member.id.equals(this.ownerID))
			this.owner = member;
		if (!exists && this.loader.ready) {
			this.loader.emit(Constants.Events.GUILD_MEMBER_ADD, member);
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
			this.loader.emit(Constants.Events.GUILD_MEMBER_ADD, member);
		}
		
		return member;
	}

	public Role addRole(RoleJSON guildRole) {
		boolean exists = this.roles.containsKey(guildRole.id);
		Role role = new Role(this, guildRole);
		this.roles.put(role.id, role);
		if (!exists && this.loader.ready) {
			this.loader.emit(Constants.Events.GUILD_ROLE_CREATE, role);
		}
		return role;
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
	 * Deletes the Guild if loader has sufficient permissions
	 * @return CompletableFuture
	 */
	public CompletableFuture<Guild> delete() {
		return null;
	}

	
	public TextChannel getDefaultChannel() {
		return this.textChannels.get(this.id);
	}
	
	
	/**
	 * Sets the guild's name if the loader has sufficient permissions
	 * @param name
	 * @return CompletableFuture
	 */
	public CompletableFuture<Guild> setName(String name) {
		return this.loader.rest.modifyGuild(this, new JSONObject().put("name", name));
	}

	/**
	 * Sets the guild's icon if the loader has sufficient permissions
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
	 * Sets the Guild's voice region to the specified region
	 * @param region The new voice region
	 * @return {@link CompletableFuture}
	 */
	public CompletableFuture<Guild> setVoiceRegion(String region) {
		return this.loader.rest.modifyGuild(this, new JSONObject().put("region", region));
	}

	/**
	 * loads a 
	 * @param memberID the ID of the member to load
	 * @return CompletableFuture.GuildMember
	 */
	public CompletableFuture<GuildMember> loadMember(String memberID) {
		return this.loader.rest.loadGuildMember(this, memberID);
	}

	/**
	 * 
	 * @param limit The number of members to load. 
	 * @param before
	 * @return A CompletableFuture that completes with a HashMap of GuildMembers if successful, null otherwise. 
	 */
	public CompletableFuture<HashMap<String, GuildMember>> loadMembers(int limit, String before) {
		return null;
	}

	/**
	 * Creates a new {@link TextChannel}. 
	 * @param name The name of the channel
	 * @return A Future that completes with a {@link TextChannel} if successful.
	 */
	public CompletableFuture<TextChannel> createTextChannel(String name) {
		return null;
	}
	
	/**
	 * Creates a new {@link VoiceChannel}
	 * @param name The name of the channel
	 * @param bitrate The channel's bitrate
	 * @param userLimit the channel's userlimit
	 * @return
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name, int bitrate, int userLimit) {
		return null;
	}
	
	/**
	 * @param name
	 * @param bitrate
	 * @return
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name, int bitrate) {
		return null;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public CompletableFuture<VoiceChannel> createVoiceChannel(String name) {
		return null;
	}
	
	/**
	 * Creates a new {@link Role}.
	 * @param name The name of the role
	 * @param permissions The 53bit Permissions integer to assign to the role
	 * @param color The color of the role
	 * @param hoist Display role members separately from online members
	 * @param mentionable Allow anyone to @mention this role
	 * @return A future that completes with a new {@link Role} Object if successful.
	 */
	public CompletableFuture<Role> createRole(String name, int permissions, int color, boolean hoist, boolean mentionable) {
		return null;
	}
	
	
	
}