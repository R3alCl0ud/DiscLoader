package io.disc.DiscLoader.objects.structures;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MemberJSON;

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
	
	public GuildMember(Guild guild, MemberJSON data) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(data.user);
		this.id = this.user.id;
		this.guild = guild;
		this.nick = data.nick != null ? data.nick : this.user.username;
//		this.joinedAt = Date.from(Instant.parse(data.joined_at));
		this.roles = new HashMap<String, Role>();

		this.deaf = data.deaf;
		this.mute = this.deaf ? true : data.mute;
		
	}
	
	public String toString() {
		return this.user.toString();
	}
	
	public Presence getPresence() {
		return this.guild.presences.get(this.user.id);
	}
}
