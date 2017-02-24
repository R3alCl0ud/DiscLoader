package io.discloader.discloader.entity;

import java.math.BigDecimal;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.json.RoleJSON;

public class Role {
	public final String id;
	public String name;
	public int permissions;
	public int color;
	public int position;
	public boolean hoist;
	public final boolean managed;
	public boolean mentionable;

	/**
	 * The {@link Guild} the role belongs to.
	 */
	public final Guild guild;

	public final DiscLoader loader;

	public Role(Guild guild, RoleJSON role) {
		this.guild = guild;
		this.loader = this.guild.loader;
		this.id = role.id;
		this.name = role.name;
		this.color = new BigDecimal(role.color).intValue();
		this.permissions = new BigDecimal(role.permissions).intValue();
		this.position = new BigDecimal(role.position).intValue();
		this.hoist = role.hoist;
		this.mentionable = role.mentionable;
		this.managed = role.managed;
	}

	private Role(Guild guild, String id, String name, int color, int permissions, int position, boolean hoist,
			boolean mentionable, boolean managed) {
		this.guild = guild;
		this.loader = this.guild.loader;
		this.id = id;
		this.name = name;
		this.color = color;
		this.permissions = permissions;
		this.position = position;
		this.hoist = hoist;
		this.mentionable = mentionable;
		this.managed = managed;
	}

	public Role update(String name, int color, int permissions, int position, boolean hoist, boolean mentionable) {
		this.name = name;

		this.color = color;

		this.permissions = permissions;

		this.position = position;

		this.hoist = hoist;

		this.mentionable = mentionable;

		return this;
	}

	public Role update(RoleJSON role) {
		this.name = role.name;
		this.color = new BigDecimal(role.color).intValue();
		this.permissions = new BigDecimal(role.permissions).intValue();
		this.position = new BigDecimal(role.position).intValue();
		this.hoist = role.hoist;
		this.mentionable = role.mentionable;
		return this;
	}

	public Role clone() {
		return new Role(this.guild, this.id, this.name, this.color, this.permissions, this.position, this.hoist,
				this.mentionable, this.managed);
	}

}
