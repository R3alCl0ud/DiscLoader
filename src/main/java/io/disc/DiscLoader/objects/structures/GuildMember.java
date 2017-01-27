package io.disc.DiscLoader.objects.structures;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MemberJSON;
import io.disc.DiscLoader.objects.gateway.RoleJSON;

public class GuildMember {
	
	public final DiscLoader loader;
	public String nick;
	public final String id;
	public final User user;
	public final Guild guild;
	public HashMap<String, Role> roles;
	public boolean mute;
	public boolean deaf;
	
	public Date joinedAt;
	
	public GuildMember(Guild guild, MemberJSON guildUser) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(guildUser.user);
		this.id = this.user.id;
		System.out.println(this.toString());
		this.guild = guild;
		this.nick = guildUser.nick;
		this.joinedAt = Date.from(Instant.parse(guildUser.joined_at));
		
		this.roles = new HashMap<String, Role>();

		this.deaf = guildUser.deaf;
		this.mute = this.deaf ? true : guildUser.mute;
		
	}
	
	public String toString() {
		return this.user.toString();
	}
	
	public Presence getPresence() {
		return this.guild.presences.get(this.user.id);
	}
}
