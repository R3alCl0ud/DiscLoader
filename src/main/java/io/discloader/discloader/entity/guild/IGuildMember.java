package io.discloader.discloader.entity.guild;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
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
	
	CompletableFuture<IGuildMember> deafen();
	
	IGuild getGuild();
	
	DiscLoader getLoader();
	
	String getNickname();
	
	IPermission getPermissions();
	
	IPresence getPresence();
	
	/**
	 * returns a HashMap of roles that the member belongs to.
	 * 
	 * @return A List of the {@link IGuildMember member's} {@link IRole roles} sorted by {@link IRole#getPosition()}.
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
	
	CompletableFuture<IGuildMember> mute();
	
	CompletableFuture<IGuildMember> takeRole(IRole role);
	
	CompletableFuture<IGuildMember> unDeafen();
	
	CompletableFuture<IGuildMember> unMute();
	
	/**
	 * Determines the member's highest role in the {@link #guild}'s role hiarchy
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
