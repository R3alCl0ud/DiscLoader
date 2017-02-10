package io.disc.discloader.objects.structures;

import java.math.BigDecimal;

import io.disc.discloader.objects.gateway.RoleJSON;

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
		this.guild = guild;
		this.id = role.id;
		this.name = role.name;
		this.color = new BigDecimal(role.color).intValue();
		this.permissions = new BigDecimal(role.permissions).intValue();
		this.position = new BigDecimal(role.position).intValue();
		this.hoist = role.hoist;
		this.managed = role.managed;
	}

}
