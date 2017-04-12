package io.discloader.discloader.entity.guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.IInvite;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.voice.VoiceState;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.PresenceJSON;
import io.discloader.discloader.network.json.RoleJSON;

/**
 * @author Perry Berman
 */
public interface IGuild extends ISnowflake, ICreationTime {
	
	/**
	 * @param user
	 * @param roles
	 * @param b
	 * @param c
	 * @param nick
	 * @param d
	 * @return
	 */
	IGuildMember addMember(IUser user, String[] roles, boolean b, boolean c, String nick, boolean d);
	
	/**
	 * @param data
	 */
	IGuildMember addMember(MemberJSON data);
	
	/**
	 * @param role
	 * @return
	 */
	IRole addRole(RoleJSON role);
	
	CompletableFuture<IGuildMember> ban(IGuildMember member);
	
	/**
	 * @param name
	 * @param name2
	 * @return
	 */
	CompletableFuture<IGuildChannel> createChannel(String name, String name2);
	
	/**
	 * @return
	 */
	IGuildMember getCurrentMember();
	
	Map<Long, IGuildEmoji> getEmojis();
	
	CompletableFuture<List<IIntegration>> getIntegrations();
	
	CompletableFuture<List<IInvite>> getInvites();
	
	/**
	 * @return the current instance of {@link DiscLoader}
	 */
	DiscLoader getLoader();
	
	IGuildMember getOwner();
	
	IGuildMember getMember(long memberID);
	
	IGuildMember getMember(String memberID);
	
	int getMemberCount();
	
	Map<Long, IGuildMember> getMembers();
	
	String getName();
	
	IPresence getPresence(long memberID);
	
	Map<Long, IPresence> getPresences();
	
	Map<Long, IRole> getRoles();
	
	Map<Long, IGuildTextChannel> getTextChannels();
	
	Map<Long, IGuildVoiceChannel> getVoiceChannels();
	
	/**
	 * @return
	 */
	VoiceRegion getVoiceRegion();
	
	/**
	 * @return
	 */
	HashMap<Long, VoiceState> getVoiceStates();
	
	boolean hasPermission(Permissions permissions);
	
	/**
	 * @return
	 */
	boolean isAvailable();
	
	/**
	 * @return
	 */
	boolean isOwner();
	
	/**
	 * @param member
	 * @return
	 */
	boolean isOwner(IGuildMember member);
	
	/**
	 * Kicks the member from the {@link IGuild guild} if the {@link DiscLoader client} has sufficient permissions
	 * 
	 * @param guildMember The member to kick from the guild
	 * @see Permissions
	 * @return A CompletableFuture that completes with {@code this} if successful
	 * @throws PermissionsException Thrown if the current user doesn't have the {@link Permissions#KICK_MEMBERS} permission.
	 */
	CompletableFuture<IGuildMember> kickMember(IGuildMember guildMember);
	
	/**
	 * @param pe
	 */
	void setPresence(PresenceJSON pe);
	
	/**
	 * @param data
	 */
	void setup(GuildJSON data);
	
	/**
	 * @param currentState
	 */
	void updateVoiceState(VoiceState currentState);
}
