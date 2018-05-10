package io.discloader.discloader.core.entity;

import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.util.Permissions;

/**
 * This object contains the {@link #member}'s evaulated permissions integer for
 * the {@link #channel}.
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @see Permissions
 */
public class Permission implements IPermission {

	/**
	 * The {@link GuildMember} who's permissions were evaluated in the specified
	 * {@link #channel}
	 */
	private IGuildMember member = null;

	/**
	 * The channel in which the {@link #member}'s permissions were evaluated.
	 */
	private IGuildChannel channel = null;

	private IRole role = null;

	/**
	 * The raw 53bit permissions {@link Integer}
	 */
	private final long raw;

	public Permission(IGuildMember member, IGuildChannel channel, long raw) {
		this.member = member;
		this.channel = channel;
		this.raw = raw;
	}

	public Permission(IRole role, long permissions) {
		this(null, null, permissions);
		this.role = role;
	}

	public Permission(IRole role, long permissions, IGuildChannel channel) {
		this(null, channel, permissions);
		this.role = role;
	}

	public Permission(IGuildMember member, long permissions) {
		this(member, null, permissions);
	}

	/**
	 * @return the member
	 */
	public IGuildMember getMember() {
		return member;
	}

	/**
	 * @return the channel
	 */
	@Override
	public IGuildChannel getChannel() {
		return channel;
	}

	/**
	 * @return the role
	 */
	public IRole getRole() {
		return role;
	}

	@Override
	public long toLong() {
		return raw;
	}

	@Override
	public boolean hasPermission(Permissions permission, boolean explicit) {
		return (!explicit && ((raw & Permissions.ADMINISTRATOR.getValue()) > 0) || (member != null) && member.isOwner()) || ((raw & permission.getValue()) > 0);
	}

	@Override
	public boolean hasPermission(Permissions... permissions) {
		if ((raw & Permissions.ADMINISTRATOR.getValue()) > 0)
			return true;
		for (Permissions permission : permissions) {
			if (!hasPermission(permission, false))
				return false;
		}
		return true;
	}

	@Override
	public boolean hasAny(Permissions... permissions) {
		for (Permissions permission : permissions) {
			if (hasPermission(permission, false))
				return true;
		}
		return false;
	}

}
