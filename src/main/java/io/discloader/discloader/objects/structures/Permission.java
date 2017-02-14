package io.discloader.discloader.objects.structures;

import io.discloader.discloader.util.constants;

/**
 * @author Perry Berman
 *
 */
public class Permission {
	
	
	/**
	 * The GuildMember this belongs to. 
	 */
	public final GuildMember member;

	/**
	 * The raw 53bit permissions integer
	 */
	public final int raw;

	public Permission(GuildMember member, int raw) {
		this.member = member;
		this.raw = raw;
	}

	/**
	 * @param permission A {@link constants.PermissionFlags Permission Flag}
	 * @param explicit Whether or not the member explicitly has the permission
	 * @return true, if the user has the specified permission
	 */
	public boolean hasPermission(int permission, boolean explicit) {
		if (!explicit && (this.raw & constants.PermissionFlags.ADMINISTRATOR) > 0)
			return true;
		return (this.raw & permission) > 0;
	}
}
