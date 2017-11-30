package io.discloader.discloader.entity.guild;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.voice.VoiceState;

/**
 * @author Perry Berman
 */
public interface IGuildMember extends ISnowflake, IMentionable {

	OffsetDateTime getJoinTime();

	CompletableFuture<IGuildMember> ban();

	CompletableFuture<IGuildMember> ban(String reason);

	CompletableFuture<IGuildMember> deafen();

	IGuild getGuild();

	DiscLoader getLoader();

	String getNickname();

	IPermission getPermissions();

	IPresence getPresence();

	/**
	 * returns a HashMap of roles that the member belongs to.
	 * 
	 * @return A List of the {@link IGuildMember member's} {@link IRole roles}
	 *         sorted by {@link IRole#getPosition()}.
	 */
	List<IRole> getRoles();

	IUser getUser();

	boolean hasRole(IRole role);

	CompletableFuture<IGuildMember> setNick(String nick);

	IGuildVoiceChannel getVoiceChannel();

	VoiceState getVoiceState();

	CompletableFuture<IGuildMember> giveRole(IRole... roles);

	boolean isDeafened();

	boolean isMuted();

	boolean isOwner();

	CompletableFuture<IGuildMember> kick();

	CompletableFuture<IGuildMember> kick(String reason);

	CompletableFuture<IGuildMember> mute();

	/**
	 * Takes a role away from a member
	 * 
	 * @param roles
	 *            The role(s) to take away from the member
	 * @return A Future that completes with the member if successful.
	 * @throws PermissionsException
	 *             thrown if a role with a higher position than the current user's
	 *             highest role is attempted to be given to the member. Also thrown
	 *             if the current user doesn't have the MANAGE_ROLE permission.
	 */
	CompletableFuture<IGuildMember> takeRole(IRole... roles);

	CompletableFuture<IGuildMember> unDeafen();

	/**
	 * @return A CompletableFuture that completes with the new {@link IGuildMember}
	 *         object if successful.
	 */
	CompletableFuture<IGuildMember> unMute();

	/**
	 * Determines the member's highest role in the {@link #getGuild guild}'s role
	 * hiarchy
	 * 
	 * @return A Role object
	 */
	IRole getHighestRole();

	/**
	 * @param channel
	 * @return
	 */
	CompletableFuture<IGuildMember> move(IGuildVoiceChannel channel);
}
