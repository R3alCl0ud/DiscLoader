package io.discloader.discloader.entity.impl;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.Permission;
import io.discloader.discloader.entity.channels.Channel;

public interface IGuildChannel {
	
	/**
	 * A {@link HashMap} of the channel's {@link GuildMember members}. Indexed
	 * by {@link GuildMember #id member.id}. <br>
	 * Is {@code null} if {@link #guild} is {@code null}, and if {@link #type}
	 * is {@code "dm"}, or {@code "groupDM"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
	
	CompletableFuture<Channel> setName(String name);
	
	CompletableFuture<Channel> setPosition(int position);
	
	CompletableFuture<Channel> setPermissions(int allow, int deny, String type);

	HashMap<String, GuildMember> getMembers();
	
	Permission permissionsFor(GuildMember member);

	HashMap<String, Overwrite> overwritesOf(GuildMember member);

}
