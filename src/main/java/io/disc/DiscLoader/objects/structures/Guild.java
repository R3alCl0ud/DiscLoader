package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.objects.gateway.MemberJSON;

public class Guild {
	public String id;
	public String name;
	public String ownerID;
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
		
	}
	
	public void addMember(MemberJSON guildUser) {
		
	}
}
