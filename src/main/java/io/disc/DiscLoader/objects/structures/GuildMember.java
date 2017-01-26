package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.objects.gateway.MemberJSON;

public class GuildMember {
	public User user;
	public Guild guild;
	public String nick;
	public HashMap<String, Role> roles;

	public GuildMember(Guild guild, MemberJSON guildUser) {
		this.guild = guild;
	}
}
