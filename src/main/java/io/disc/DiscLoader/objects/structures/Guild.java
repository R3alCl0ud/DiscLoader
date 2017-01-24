package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.objects.gateway.GuildGateway;
import io.disc.DiscLoader.objects.gateway.UserGateway;

public class Guild {
	public String id;
	public String name;
	public String ownerID;
	public boolean available;
	
	public HashMap<String, GuildMember> members;
	
	public HashMap<String, GuildChannel> channels;
	
	public HashMap<String, Role> roles;
	
	public Guild(GuildGateway guild) {
		this.members = new HashMap<String, GuildMember>();
		
		
		if (guild.unavailable) {
			this.available = false;
			this.id = guild.id;
		} else {
			this.available = true;
			this.setup(guild);
		}
		
	}
	
	public void setup(GuildGateway guild) {
		
	}
	
	public void addMember(UserGateway guildUser) {
		
	}
}
