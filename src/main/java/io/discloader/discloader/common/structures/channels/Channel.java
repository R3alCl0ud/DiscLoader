package io.discloader.discloader.common.structures.channels;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.structures.Guild;
import io.discloader.discloader.common.structures.GuildMember;
import io.discloader.discloader.common.structures.Overwrite;
import io.discloader.discloader.common.structures.Permission;
import io.discloader.discloader.common.structures.Role;
import io.discloader.discloader.common.structures.User;
import io.discloader.discloader.network.gateway.json.ChannelJSON;
import io.discloader.discloader.util.Constants;

public class Channel {

	public String id;
	public String name;
	public String topic;
	public String lastMessageID;
	public String type;

	public int bitrate;
	public int userLimit;
	public int position;
	
	public boolean isPrivate;

	public final DiscLoader loader;

	public User user;

	/**
	 * The {@link Guild} the channel belongs to.
	 * <br>This property <u>must</u> be {@code null} if the {@link #type} of the channel is {@code "dm"}, or {@code "groupDM"}.
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final Guild guild;
	
	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by {@link User#id}.
	 * <br>Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, User> recipients;
	
	/**
	 * A {@link HashMap} of the channel's {@link Overwrite overwrites}. Indexed by {@link Overwrite#id}.
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, Overwrite> overwrites;
	
	/**
	 * A {@link HashMap} of the channel's {@link GuildMember members}. Indexed by {@link GuildMember #id member.id}.
	 * <br> Is {@code null} if {@link #guild} is {@code null}, and if {@link #type} is {@code "dm"}, or {@code "groupDM"}.
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, GuildMember> members;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		this.type = null;

		this.guild = null;

		this.recipients = new HashMap<String, User>();

		this.members = null;

		this.overwrites = new HashMap<String, Overwrite>();

		if (data != null)
			this.setup(data);
	}

	public Channel(Guild guild, ChannelJSON data) {
		this.loader = guild.loader;

		this.guild = guild;

		this.type = null;

		this.recipients = null;

		this.members = new HashMap<String, GuildMember>();

		this.overwrites = new HashMap<String, Overwrite>();

		if (data != null)
			this.setup(data);
	}

	public void setup(ChannelJSON data) {
		this.id = data.id;
		
		this.isPrivate = data.is_private;
	}

	/**
	 * @return
	 */
	public HashMap<String, GuildMember> getMembers() {
		HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
		for (GuildMember member : this.guild.members.values()) {
			if (this.permissionsFor(member).hasPermission(Constants.PermissionFlags.READ_MESSAGES, false))
				members.put(member.id, member);
		}
		return members;
	}

	/**
	 * Evaluates a 
	 * @param member The member whose permissions we are evaluating.
	 * @return A new Permissions object that contains {@literal this}, the {@literal member}, and their evaluated permissions {@link Integer}.
 	 * <br>null if the channel doesn't belong to a {@link #guild}
	 */
	public Permission permissionsFor(GuildMember member) {
		int raw = 0;
		if (member.id == this.guild.ownerID)
			return new Permission(member, this, 2146958463);
		for (Role role : member.getRoleList().values())
			raw |= role.permissions;
		for (Overwrite overwrite : this.overwritesOf(member).values()) {
			raw |= overwrite.allow;
			raw &= ~overwrite.deny;
		}
		return new Permission(member, this, raw);
	}

	/**
	 * Gets all of the channel's {@link #overwrites} that applies to a {@link GuildMember}
	 * @param member The member of whome we are looking for overwrites that apply. 
	 * @author Perry Berman
	 * @return A {@link HashMap} of overwrite objects, indexed by {@link Overwrite#id}
	 * @since 0.0.1
	 */
	public HashMap<String, Overwrite> overwritesOf(GuildMember member) {
		HashMap<String, Overwrite> Overwrites = new HashMap<String, Overwrite>();
		for (Role role : member.getRoleList().values()) {
			if (this.overwrites.get(role.id) != null)
				Overwrites.put(role.id, this.overwrites.get(role.id));
		}
		if (this.overwrites.get(member.id) != null)
			Overwrites.put(member.id, this.overwrites.get(member.id));
		return Overwrites;
	}
}
