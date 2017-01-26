package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MemberJSON;
import io.disc.DiscLoader.objects.gateway.RoleJSON;

public class GuildMember {
	public DiscLoader loader;
	public User user;
	public Guild guild;
	public String nick;
	public HashMap<String, Role> roles;

	public GuildMember(Guild guild, MemberJSON guildUser) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(guildUser.user);
		this.guild = guild;
		this.nick = guildUser.nick;
		this.roles = new HashMap<String, Role>();
		for (RoleJSON roleJSON : guildUser.roles) {
			
		}
	}
}
