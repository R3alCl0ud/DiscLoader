package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;
import io.disc.DiscLoader.objects.gateway.OverwriteJSON;
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
		
		if (data.permission_overwrites != null) {
			for (OverwriteJSON overwrite : data.permission_overwrites) {
				
			}
		}
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
		int raw = 0;
		if (member.id == this.guild.ownerID)
			return new Permission(member, 2146958463);
		for (Role role : member.getRoleList().values())
			raw |= role.permissions;
		return new Permission(member, raw);
	}
	
	public Overwrite overwritesOf(GuildMember member) {
		
		return null;
	}

}
