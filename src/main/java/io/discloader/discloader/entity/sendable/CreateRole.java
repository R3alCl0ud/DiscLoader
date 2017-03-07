package io.discloader.discloader.entity.sendable;

public class CreateRole {
	public String name;
	public int perimissions;
	public int color;
	public boolean hoist;
	public boolean mentionable;

	public CreateRole(String name, int permissions, int color, boolean hoist, boolean mentionable) {
		this.name = name;
		this.perimissions = permissions;
		this.color = color;
		this.hoist = hoist;
		this.mentionable = mentionable;
	}
}
