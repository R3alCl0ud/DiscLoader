package io.discloader.discloader.core.entity;

import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.util.DLUtil;

/**
 * This object contains the {@link #member}'s evaulated permissions integer for
 * the {@link #channel}.
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @see DLUtil.PermissionFlags
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

	private Role role = null;

	/**
	 * The raw 53bit permissions {@link Integer}
	 */
	private final int raw;

	public Permission(IGuildMember member, IGuildChannel channel, int raw) {
		this.member = member;
		this.channel = channel;
		this.raw = raw;
	}

	public Permission(Role role, int permissions) {
		this(null, null, permissions);
		this.role = role;
	}

	public Permission(IGuildMember member, int permissions) {
		this(member, null, permissions);
	}

	/**
	 * Checks if the member has the specified permission.
	 * 
	 * @param permission A {@link DLUtil.PermissionFlags Permission Flag}
	 * @return {@code true}, if the user has the specified permission. Otherwise
	 *         {@code false}.
	 */
	public boolean hasPermission(int permission) {
		return this.hasPermission(permission, false);
	}

	/**
	 * Checks if the member has the specified permission. <br>
	 * {@code boolean sendMessages = channel.permissionsFor(member).hasPermission(PermissionFlags.SEND_MESSAGES);}
	 * 
	 * @param permission A {@link DLUtil.PermissionFlags Permission Flag}
	 * @param explicit Whether or not the member explicitly has the permission
	 * @return {@code true}, if the user has the specified permission. Otherwise
	 *         {@code false}.
	 */
	public boolean hasPermission(int permission, boolean explicit) {
		if (!explicit && (this.raw & Permissions.ADMINISTRATOR.getValue()) > 0) return true;
		return (this.raw & permission) > 0;
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
	public Role getRole() {
		return role;
	}

	public int asInteger() {
		return raw;
	}

	/*
	 * (non-Javadoc)
	 * @see io.discloader.discloader.entity.IPermission#toInt()
	 */
	@Override
	public int asInt() {
		return raw;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * io.discloader.discloader.entity.IPermission#hasPermission(io.discloader.
	 * discloader.entity.Permissions)
	 */
	@Override
	public boolean hasPermission(Permissions permission) {
		return hasPermission(permission, false);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * io.discloader.discloader.entity.IPermission#hasPermission(io.discloader.
	 * discloader.entity.Permissions, boolean)
	 */
	@Override
	public boolean hasPermission(Permissions permission, boolean explicit) {
		if (!explicit && (this.raw & Permissions.ADMINISTRATOR.getValue()) > 0) return true;
		return (this.raw & permission.getValue()) > 0;
	}

}
