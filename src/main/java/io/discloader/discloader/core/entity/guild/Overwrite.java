package io.discloader.discloader.core.entity.guild;

import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.OverwriteJSON;

/**
 * Permission Overwrite object
 * 
 * @author Perry Berman
 */
public class Overwrite implements IOverwrite {

	/**
	 * The id of the {@link Role} or {@link GuildMember} the overwrite applies to.
	 */
	public transient long id;

	/**
	 * The 53bit integer of allowed permissions
	 */
	public long allow;

	/**
	 * The 53bit integer of denied permissions
	 */
	public long deny;

	/**
	 * either "role" or "member"
	 */
	public String type;

	/**
	 * The {@link Role} the overwrite applies to. null if type is
	 * {@literal "member"}
	 */
	public transient IRole role;

	/**
	 * The {@link GuildMember} the overwrite applies to. null if type is
	 * {@literal "role"}
	 * 
	 *
	 */
	public transient IGuildMember member;

	private transient IGuildChannel channel;

	public Overwrite(IGuildMember member) {
		this(0, 0, member);
	}

	public Overwrite(IRole role) {
		this(0, 0, role);
	}

	public Overwrite(long allow, long deny) {
		this.allow = allow;
		this.deny = deny;
	}

	public Overwrite(long allow, long deny, IGuildMember member) {
		this(allow, deny);
		type = "member";
		id = member.getID();
	}

	public Overwrite(long allow, long deny, IRole role) {
		this(allow, deny);
		type = "role";
		id = role.getID();
	}

	public Overwrite(Overwrite data, IGuildMember member) {
		this(data.getAllowed(), data.getDenied());
		this.type = "member";
		id = member.getID();
	}

	public Overwrite(Overwrite data, IRole role) {
		this(data.getAllowed(), data.getDenied());
		type = "role";
		id = role.getID();
	}

	public Overwrite(OverwriteJSON data) {
		this(data.allow, data.deny);
		if (data.id != null)
			this.id = SnowflakeUtil.parse(data.id);
		if (data.type != null)
			this.type = data.type;
	}

	public long getAllowed() {
		return allow;
	}

	public IGuildChannel getChannel() {
		return channel;
	}

	public long getDenied() {
		return deny;
	}

	@Override
	public long getID() {
		return id;
	}

	public IGuildMember getMember() {
		return channel.getGuild().getMember(id);
	}

	public IRole getRole() {
		return channel.getGuild().getRoleByID(id);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setAllowed(Permissions... permissions) {
		for (Permissions p : permissions) {
			allow |= p.getValue();
		}
	}

	@Override
	public void setDenied(Permissions... permissions) {
		for (Permissions p : permissions) {
			deny |= p.getValue();
		}
	}

	@Override
	public IPermission computePermissions() {
		switch (type) {
		case "member":
			return channel.permissionsOf(getMember());
		case "role":
			return channel.permissionsOf(getRole());
		default:
			return null;
		}
	}

}
