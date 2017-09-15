package io.discloader.discloader.entity.guild;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.GuildSyncException;
import io.discloader.discloader.common.exceptions.MissmatchException;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.core.entity.channel.VoiceChannel;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.network.json.RoleJSON;

/**
 * This represents a Guild in Discord's API
 * 
 * @author Perry Berman
 * @since 0.1.0
 */
public interface IGuild extends ISnowflake, ICreationTime {

	IGuildMember addMember(IGuildMember member);

	IGuildMember addMember(IGuildMember member, boolean emit);

	IGuildMember addMember(IUser user, String[] roles, boolean b, boolean c, String nick, boolean d);

	IGuildMember addMember(MemberJSON data);

	IGuildMember addMember(MemberJSON data, boolean emit);

	IRole addRole(IRole role);

	IRole addRole(RoleJSON role);

	/**
	 * Bans the member from the {@link Guild} if the {@link DiscLoader loader}
	 * has sufficient permissions
	 *
	 * @param member The member to ban from the guild
	 * @see Permissions
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 * @throws PermissionsException
	 */
	CompletableFuture<IGuildMember> ban(IGuildMember member) throws PermissionsException;

	/**
	 * Begin a prune operation. Requires the {@link Permissions#KICK_MEMBERS
	 * KICK_MEMBERS} permission.
	 * 
	 * @return A Future that completes with the number of member kicked during
	 *         the prune operation if successful.
	 * @throws PermissionsException if the current user doesn't have the
	 *             {@link Permissions#KICK_MEMBERS} permission.
	 */
	CompletableFuture<Integer> beginPrune();

	/**
	 * Begin a prune operation. Requires the {@link Permissions#KICK_MEMBERS
	 * KICK_MEMBERS} permission.
	 * 
	 * @param days The number of days to prune (1 or more)
	 * @return A Future that completes with the number of member kicked during
	 *         the prune operation if successful.
	 * @throws PermissionsException if the current user doesn't have the
	 *             {@link Permissions#KICK_MEMBERS KICK_MEMBERS} permission.
	 */
	CompletableFuture<Integer> beginPrune(int days);

	// /**
	// * @param name
	// * @param name2
	// * @return
	// */
	// CompletableFuture<IGuildChannel> createChannel(String name, String
	// name2);

	IGuild clone();

	CompletableFuture<IChannelCategory> createCategory(String name);

	CompletableFuture<IChannelCategory> createCategory(String name, IOverwrite... overwrites);

	/**
	 * Creates a new custom emoji
	 * 
	 * @param name The name of the new emoji
	 * @param image The file
	 * @return A Future the completes with the created Emoji if successful.
	 */
	CompletableFuture<IGuildEmoji> createEmoji(String name, File image);

	/**
	 * Creates a new custom emoji
	 * 
	 * @param name The name of the emoji
	 * @param image The emoji's image encoded to base64
	 * @return A Future the completes with the created Emoji if successful.
	 */
	CompletableFuture<IGuildEmoji> createEmoji(String name, String image);

	CompletableFuture<IRole> createRole(String name);

	/**
	 * Creates a new {@link IRole}.
	 * 
	 * @param name The name of the role
	 * @param permissions The 53bit Permissions integer to assign to the role
	 * @param color The color of the role
	 * @param hoist Display role members separately from online members
	 * @param mentionable Allow anyone to mention this role
	 * @return A future that completes with a new {@link IRole} Object if
	 *         successful.
	 * @since 0.0.3
	 */
	CompletableFuture<IRole> createRole(String name, long permissions, int color, boolean hoist, boolean mentionable);

	CompletableFuture<IGuildTextChannel> createTextChannel(String name);

	CompletableFuture<IGuildTextChannel> createTextChannel(String name, IOverwrite... overwrites);

	CompletableFuture<IGuildVoiceChannel> createVoiceChannel(String name);

	CompletableFuture<IGuildTextChannel> createVoiceChannel(String name, int bitRate, IOverwrite... overwrites);

	CompletableFuture<IGuildTextChannel> createVoiceChannel(String name, IOverwrite... overwrites);

	/**
	 * Deletes the Guild if the user you have logged in as is the owner of the
	 * guild
	 * 
	 * @return A Future that completes with {@code this} if successful, and
	 *         fails with a {@link UnauthorizedException}
	 * @throws UnauthorizedException Thrown if you are not the owner of the
	 *             guild.
	 */
	CompletableFuture<IGuild> delete();

	/**
	 * @param name The guild's new name.
	 * @param icon The guild's new icon.
	 * @param afkChannel
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException Thrown if the current user doesn't have the
	 *             {@link Permissions#MANAGE_GUILD} permission.
	 * @throws IOException
	 */
	CompletableFuture<IGuild> edit(String name, String icon, IGuildVoiceChannel afkChannel) throws IOException;

	/**
	 * Fetches a GuildMember from the REST API
	 * 
	 * @param memberID the ID of the member to load
	 * @return CompletableFuture.GuildMember
	 */
	CompletableFuture<IGuildMember> fetchMember(long memberID);

	/**
	 * Requests the Gateway to send a {@link GuildMembersChunkEvent} containing
	 * this {@link IGuild guild's} members
	 * 
	 * @return A Map of {@link IGuildMember} objects indexed by
	 *         {@link IGuildMember#getID()}
	 */
	CompletableFuture<Map<Long, IGuildMember>> fetchMembers();

	/**
	 * Requests the Gateway to send a {@link GuildMembersChunkEvent} containing
	 * this {@link IGuild guild's} members
	 * 
	 * @param limit The maximum number of members to fetch.
	 * @return A Map of {@link IGuildMember} objects indexed by
	 *         {@link IGuildMember#getID()}
	 */
	CompletableFuture<Map<Long, IGuildMember>> fetchMembers(int limit);

	Map<Long, IGuildChannel> getChannels();
	
	Map<Long, IChannelCategory> getChannelCategories();

	IChannelCategory getChannelCategoryByID(long id);

	IChannelCategory getChannelCategoryByID(String id);

	IChannelCategory getChannelCategoryByName(String name);

	/**
	 * Returns the {@link GuildMember} object for the {@link User} you are
	 * logged in as.
	 * 
	 * @return A GuildMember object
	 */
	IGuildMember getCurrentMember();

	/**
	 * Gets the guild's default text channel. The "default" channel for a given
	 * user is now the channel with the highest position that their
	 * {@link Permissions} allow them to see.
	 * 
	 * @return the {@link IGuildTextChannel} with the highest position that your
	 *         {@link Permissions} allow you to see.
	 */
	IGuildTextChannel getDefaultChannel();

	/**
	 * @return
	 */
	IRole getDefaultRole();

	Map<Long, IGuildEmoji> getEmojis();

	IIcon getIcon();

	CompletableFuture<List<IIntegration>> getIntegrations();

	CompletableFuture<List<IInvite>> getInvites();

	/**
	 * @return the current instance of {@link DiscLoader}
	 */
	DiscLoader getLoader();

	IGuildMember getMember(long memberID);

	IGuildMember getMember(String memberID);

	int getMemberCount();

	Map<Long, IGuildMember> getMembers();

	String getName();

	IGuildMember getOwner();

	long getOwnerID();

	IPresence getPresence(long memberID);

	Map<Long, IPresence> getPresences();

	/**
	 * same as {@link #getPruneCount(int days)} but only grabs one days worth.
	 * Requires the {@link Permissions#KICK_MEMBERS} permission.
	 * 
	 * @return A Future that completes with the number of members that would be
	 *         kicked in a prune operation
	 */
	CompletableFuture<Integer> getPruneCount();

	/**
	 * Returns the number of members that would be removed in a prune operation.
	 * Requires the {@link Permissions#KICK_MEMBERS} permission.
	 * 
	 * @param days The number of days to count prune for (1 or more)
	 * @return A Future that completes with the number of members that would be
	 *         kicked in a prune operation
	 */
	CompletableFuture<Integer> getPruneCount(int days);

	IRole getRoleByID(long roleID);

	IRole getRoleByID(String roleID);

	IRole getRoleByName(String name);

	Map<Long, IRole> getRoles();

	IGuildTextChannel getTextChannelByID(long channelID);

	IGuildTextChannel getTextChannelByID(String channelID);

	IGuildTextChannel getTextChannelByName(String channelName);

	Map<Long, IGuildTextChannel> getTextChannels();

	IGuildVoiceChannel getVoiceChannelByID(long channelID);

	IGuildVoiceChannel getVoiceChannelByID(String channelID);

	IGuildVoiceChannel getVoiceChannelByName(String channelName);

	Map<Long, IGuildVoiceChannel> getVoiceChannels();

	VoiceConnection getVoiceConnection();

	VoiceRegion getVoiceRegion();

	CompletableFuture<List<VoiceRegion>> getVoiceRegions();

	/**
	 * returns a Map of the guild's members' voice states. Indexed by
	 * {@link GuildMember#getID()}
	 * 
	 * @return A Map of {@link VoiceState VoiceStates}
	 */
	Map<Long, VoiceState> getVoiceStates();

	/**
	 * @param permissions
	 * @return a boolean.
	 */
	boolean hasPermission(Permissions... permissions);

	/**
	 * @return
	 */
	boolean isAvailable();

	/**
	 * Whether or not the guild has at least 250 members.
	 * 
	 * @return {@code if} {@link #memberCount} is greater than or equal to 250,
	 *         {@code false} otherwise.
	 */
	boolean isLarge();

	/**
	 * Checks whether or not the {@link IUser user} you are logged in as is the
	 * owner of the {@link IGuild guild}
	 * 
	 * @return {@code true} if {@link IGuildMember#getID()
	 *         getCurrentMember().getID()} is equal to {@link #getOwnerID()},
	 *         false otherwise.
	 */
	boolean isOwner();

	/**
	 * Checks if a {@link IGuildMember member} is the {@link #getOwner() owner}
	 * of the {@link IGuild guild}.
	 * 
	 * @param member The member to check if they own the {@link IGuild guild}.
	 * @return {@code true} if {@link IGuildMember#getID() member.getID()} is
	 *         equal to {@link #getOwnerID() this.getOwnerID()}, false
	 *         otherwise.
	 */
	boolean isOwner(IGuildMember member);

	/**
	 * Checks if the guild is currently being synced with discord
	 * 
	 * @return {@code true} if syncing, false otherwise.
	 */
	boolean isSyncing();

	/**
	 * Kicks the member from the {@link IGuild guild} if the {@link DiscLoader
	 * client} has sufficient permissions
	 * 
	 * @param guildMember The member to kick from the guild
	 * @see Permissions
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful
	 * @throws PermissionsException Thrown if the current user doesn't have the
	 *             {@link Permissions#KICK_MEMBERS} permission.
	 */
	CompletableFuture<IGuildMember> kickMember(IGuildMember guildMember);

	/**
	 * Makes the client leave the guild
	 * 
	 * @return A Future that completes with {@code this} if successful.
	 */
	CompletableFuture<IGuild> leave();

	IGuildMember removeMember(IGuildMember member);

	void removeMember(IUser user);

	IRole removeRole(IRole role);

	IRole removeRole(long roleID);

	IRole removeRole(String roleID);

	/**
	 * @param channel The {@link VoiceChannel} to set as the afk channel.
	 * @throws PermissionsException
	 * @throws MissmatchException
	 */
	CompletableFuture<IGuild> setAFKChannel(IGuildVoiceChannel afkChannel);

	/**
	 * Sets the guild's icon if the loader has sufficient permissions
	 * 
	 * @param icon location of icon file on disk
	 * @return A Future that completes with {@code this} if successful.
	 * @throws IOException Exception thrown if there is an error reading icon
	 *             file
	 * @throws PermissionsException
	 */
	CompletableFuture<IGuild> setIcon(String icon) throws IOException;

	/**
	 * Sets the guild's name if the loader has sufficient permissions
	 * 
	 * @param name The guild's new name
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException
	 */
	CompletableFuture<IGuild> setName(String name);

	/**
	 * @param member The member to set as the {@link IGuild guild's}
	 *            {@link #getOwner() owner}.
	 * @return A Future that completes with <strike>the new {@link #getOwner()
	 *         owner} of the {@link IGuild guild} if successful.</strike>
	 *         {@link IGuild this} if successful.
	 * @throws UnauthorizedException Thrown if you are not the owner of theS
	 *             guild.
	 */
	CompletableFuture<IGuild> setOwner(IGuildMember member);

	/**
	 * @param pe
	 */
	void setPresence(PresenceJSON pe);

	/**
	 * @param data
	 */
	void setup(GuildJSON data);

	/**
	 * Sets the Guild's voice region to the specified region
	 * 
	 * @param region The new voice region
	 * @return A Future that completes with {@code this} if successful.
	 * @throws PermissionsException
	 */
	CompletableFuture<IGuild> setVoiceRegion(String region);

	/**
	 * Syncs the guild to the client if the logged in user is not a bot.
	 * 
	 * @throws GuildSyncException Thrown if {@link #isSyncing()} returns true.
	 * @throws AccountTypeException Thrown if client is logged in as a bot
	 *             account
	 */
	void sync() throws GuildSyncException, AccountTypeException;

	/**
	 * @param currentState
	 */
	void updateVoiceState(VoiceState currentState);
}
