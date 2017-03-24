package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channels.impl.Channel;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.Role;
import io.discloader.discloader.network.json.OverwriteJSON;

/**
 * Permission Overwrite object
 * @author Perry Berman
 *
 */
public class Overwrite {

	/**
	 * The id of the {@link Role} or {@link GuildMember} the overwrite applies to.
	 */
	public String id;
	
	
	/**
	 * The 53bit integer of allowed permissions
	 */
	public int allow;
	
	
	/**
	 * The 53bit integer of denied permissions
	 */
	public int deny;
	
	
	/**
	 * 	either "role" or "member"
	 */
	public String type;

	/**
	 * The {@link Role} the overwrite applies to. null if type is {@literal "member"} 
	 */
	public final Role role;

	/**
	 * The {@link GuildMember} the overwrite applies to. null if type is {@literal "role"}
	 * @author cloud
	 */
	public final GuildMember member;

	/**
	 * The {@link Channel} the overwrite belongs to
	 */
	public Channel channel;

	public Overwrite(Overwrite data, GuildMember member) {
		this.allow = data.allow;
		this.deny = data.deny;
		this.type = data.type;
		this.member = member;
		this.role = null;
	}

	public Overwrite(Overwrite data, Role role) {
		this.allow = data.allow;
		this.deny = data.deny;
		this.type = data.type;
		this.role = role;
		this.member = null;
	}

	public Overwrite(OverwriteJSON data) {
		this.allow = data.allow;
		this.deny = data.deny;
		if (data.id != null)
			this.id = data.id;
		if (data.type != null)
			this.type = data.type;
		this.role = null;
		this.member = null;
	}

}
