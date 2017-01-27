package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.RoleJSON;

public class Role {
	public final String id;
	public String name;
	public int permissions;
	public int color;
	public int position;
	public boolean hoist;
	public final boolean managed;
	public boolean mentionable;

	public final Guild guild;
	
	public Role(Guild guild, RoleJSON role) {
		this.id = role.id;
		this.name = role.name;
		this.color = role.color;
		this.permissions = role.permissions;
		this.position = role.position;
		this.hoist = role.hoist;
		this.managed = role.managed;
		this.guild = guild;
	}

}
