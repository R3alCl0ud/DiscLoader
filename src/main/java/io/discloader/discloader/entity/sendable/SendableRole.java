package io.discloader.discloader.entity.sendable;

public class SendableRole {

	public String name;
	public int permissions;
	public int color;
	public boolean hoist;
	public boolean mentionable;

	public SendableRole(String name, int permissions, int color, boolean hoist, boolean mentionable) {
		this.name = name;
		this.permissions = permissions;
		this.color = color;
		this.hoist = hoist;
		this.mentionable = mentionable;
	}

}
