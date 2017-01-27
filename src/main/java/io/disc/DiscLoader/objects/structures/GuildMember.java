package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.MemberJSON;
import io.disc.DiscLoader.objects.gateway.RoleJSON;

public class GuildMember {
	public final DiscLoader loader;
	public final User user;
	public final Guild guild;
	public String nick;
	public HashMap<String, Role> roles;
	public Presence presence;

	
	public GuildMember(Guild guild, MemberJSON guildUser) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(guildUser.user);
		this.guild = guild;
		this.nick = guildUser.nick;
		this.roles = new HashMap<String, Role>();
		for (RoleJSON roleJSON : guildUser.roles) {
			if (this.guild.roles.containsKey(roleJSON.id)) {
				Role role = this.guild.roles.get(roleJSON.id);
				this.roles.put(role.id, role);
			} else {
				Role role = this.guild.addRole(roleJSON);
				this.roles.put(role.id, role);
			}
		}
	}
	
	public String toString() {
		return this.user.toString();
	}
}
