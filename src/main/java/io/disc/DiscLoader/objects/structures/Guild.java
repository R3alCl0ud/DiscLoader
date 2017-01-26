package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.objects.gateway.MemberJSON;
import io.disc.DiscLoader.util.constants;

public class Guild {
	public String id;
	public String name;
	public String ownerID;
	public String icon;
	public boolean available;
	
	public DiscLoader loader;
	
	public HashMap<String, GuildMember> members;
	
	public HashMap<String, Channel> channels;
	
	public HashMap<String, Role> roles;
	
	public Guild(DiscLoader loader, GuildJSON guild) {
		this.loader = loader;
		
		this.members = new HashMap<String, GuildMember>();
		this.channels = new HashMap<String, Channel>();
		
		if (guild.unavailable) {
			this.available = false;
			this.id = guild.id;
		} else {
			this.available = true;
			this.setup(guild);
		}
		
	}
	
	public void setup(GuildJSON guild) {
		this.name = guild.name;
		this.icon = guild.icon != null ? guild.icon : null;
		if (guild.members != null) {
			for (MemberJSON member : guild.members) {
				this.addMember(member);
			}
		}
		
	}
	
	public GuildMember addMember(MemberJSON guildUser) {
		boolean exists = this.members.containsKey(guildUser.user.id);
		GuildMember member = new GuildMember(this, guildUser);
		this.members.put(member.user.id, member);
		if (!exists && this.loader.ready) {
			this.loader.emit(constants.Events.GUILD_MEMBER_ADD, member);
		}
		return member;
	}
}
