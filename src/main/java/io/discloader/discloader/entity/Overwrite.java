package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channels.impl.GuildChannel;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.Role;
import io.discloader.discloader.network.json.OverwriteJSON;

/**
 * Permission Overwrite object
 * 
 * @author Perry Berman
 */
public class Overwrite {

	/**
	 * The id of the {@link Role} or {@link GuildMember} the overwrite applies
	 * to.
	 */
	public transient String id;

	/**
	 * The 53bit integer of allowed permissions
	 */
	public int allow;

	/**
	 * The 53bit integer of denied permissions
	 */
	public int deny;

	/**
	 * either "role" or "member"
	 */
	public String type;

	/**
	 * The {@link Role} the overwrite applies to. null if type is
	 * {@literal "member"}
	 */
	public transient Role role;

	/**
	 * The {@link GuildMember} the overwrite applies to. null if type is
	 * {@literal "role"}
	 * 
	 * @author Perry Berman
	 */
	public transient GuildMember member;

	private transient GuildChannel channel;

	public Overwrite(int allow, int deny) {
		this.allow = allow;
		this.deny = deny;
	}

	public Overwrite(int allow, int deny, GuildMember member) {
		this(allow, deny);
		type = "member";
		id = member.id;
	}

	public Overwrite(int allow, int deny, Role role) {
		this(allow, deny);
		type = "role";
		id = role.id;
	}

	public Overwrite(Overwrite data, GuildMember member) {
		this(data.getAllowed(), data.getDenied());
		this.type = "member";
		id = member.id;
	}

	public Overwrite(Overwrite data, Role role) {
		this(data.getAllowed(), data.getDenied());
		type = "role";
		id = role.id;
	}

	public Overwrite(OverwriteJSON data) {
		this(data.allow, data.deny);
		if (data.id != null) this.id = data.id;
		if (data.type != null) this.type = data.type;
	}

	public int getAllowed() {
		return allow;
	}

	public GuildChannel getChannel() {
		return channel;
	}

	public int getDenied() {
		return deny;
	}

	public String getID() {
		return id;
	}

	public GuildMember getMember() {
		return channel.guild.members.get(id);
	}

	public Role getRole() {
		return channel.guild.roles.get(id);
	}

	public int setAllowedPermissions(Permissions... permissions) {
		for (Permissions p : permissions) {
			allow |= p.getValue();
		}
		return allow;
	}

	public int setDeniedPermissions(Permissions... permissions) {
		for (Permissions p : permissions) {
			deny |= p.getValue();
		}
		return allow;
	}

}
