package io.discloader.discloader.entity.sendable;

public class SendableRole {

	public String name;
	public long permissions;
	public int color;
	public boolean hoist;
	public boolean mentionable;

	public SendableRole(String name, long permissions, int color, boolean hoist, boolean mentionable) {
		this.name = name;
		this.permissions = permissions;
		this.color = color;
		this.hoist = hoist;
		this.mentionable = mentionable;
	}

}
