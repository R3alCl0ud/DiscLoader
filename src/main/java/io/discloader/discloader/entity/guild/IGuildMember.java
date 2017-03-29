package io.discloader.discloader.entity.guild;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.entity.Presence;
import io.discloader.discloader.common.entity.guild.GuildMember;
import io.discloader.discloader.common.entity.guild.Role;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.ISnowflake;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.voice.VoiceState;

/**
 * @author Perry Berman
 */
public interface IGuildMember extends ISnowflake {

	CompletableFuture<IGuildMember> ban();

	CompletableFuture<IGuildMember> deafen();

	IGuild getGuild();

	DiscLoader getLoader();

	String getNickname();

	String asMention();

	IPermission getPermissions();

	Presence getPresence();

	Map<String, Role> getRoles();

	IUser getUser();

	CompletableFuture<IGuildMember> setNick(String nick);

	IVoiceChannel getVoiceChannel();

	VoiceState getVoiceState();

	CompletableFuture<IGuildMember> giveRole(Role... roles);

	boolean isDeaf();

	boolean isMute();

	CompletableFuture<IGuildMember> kick();

	CompletableFuture<IGuildMember> move(IVoiceChannel voicechannel);

	CompletableFuture<IGuildMember> mute();

	CompletableFuture<GuildMember> takeRole(Role role);

	CompletableFuture<IGuildMember> unDeafen();

	CompletableFuture<IGuildMember> unMute();
}
