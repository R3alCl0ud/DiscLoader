package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;
import io.disc.DiscLoader.util.constants;
import io.disc.DiscLoader.util.constants.Permissions;

public class GuildChannel extends Channel {
	public final Guild guild;
	
	public Permissions permission;
	
	
	public GuildChannel(DiscLoader loader, Guild guild, ChannelJSON channel) {
		super(loader, channel);
		this.name = channel.name;
		this.guild = guild;
	}
	
	public HashMap<String, GuildMember> getMembers() {
		HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
		for (GuildMember member : this.guild.members.values()) {
			if (this.permissionsFor(member).hasPermission(constants.PermissionFlags.READ_MESSAGES, false))
				members.put(member.id, member);
		}
		return members;
	}
	
	public Permission permissionsFor(GuildMember member) {
		return new Permission(member, constants.Permissions.READ_MESSAGES);
	}
	
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content);
	}
}
