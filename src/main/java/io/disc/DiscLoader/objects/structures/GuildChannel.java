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

	public int position;
	
	
	public GuildChannel(Guild guild, ChannelJSON channel) {
		super(guild.loader, channel);

		this.guild = guild;
	}
	
	public void setup(ChannelJSON data) {
		super.setup(data);
		
		this.name = data.name;
		
		this.position = data.position;
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
	

}
