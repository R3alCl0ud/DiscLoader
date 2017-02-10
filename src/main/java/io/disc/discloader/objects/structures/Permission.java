/**
 * 
 */
package io.disc.discloader.objects.structures;

import io.disc.discloader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class Permission {
	public GuildMember member;

	public int raw;

	public Permission(GuildMember member, int raw) {
		this.member = member;
		this.raw = raw;
	}

	public boolean hasPermission(int permission, boolean explicit) {
		if (!explicit && (this.raw & constants.PermissionFlags.ADMINISTRATOR) > 0)
			return true;
		return (this.raw & permission) > 0;
	}

	public boolean hasPermission(String permission, boolean explicit) {
		int num = this.member.loader.resolvePermission(permission);
		if (!explicit && (this.raw & constants.PermissionFlags.ADMINISTRATOR) > 0)
			return true;
		return (this.raw & num) > 0;
	}
}
