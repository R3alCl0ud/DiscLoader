package io.discloader.discloader.objects.structures;

import java.util.HashMap;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.objects.gateway.ChannelJSON;
import io.discloader.discloader.util.constants;

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

	public final Guild guild;
	public final HashMap<String, User> recipients;
	public final HashMap<String, Overwrite> overwrites;
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
	}

	/**
	 * @return
	 */
	public HashMap<String, GuildMember> getMembers() {
		HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
		for (GuildMember member : this.guild.members.values()) {
			if (this.permissionsFor(member).hasPermission(constants.PermissionFlags.READ_MESSAGES, false))
				members.put(member.id, member);
		}
		return members;
	}

	/**
	 * @param member
	 * @return
	 */
	public Permission permissionsFor(GuildMember member) {
		int raw = 0;
		if (member.id == this.guild.ownerID)
			return new Permission(member, 2146958463);
		for (Role role : member.getRoleList().values())
			raw |= role.permissions;
		for (Overwrite overwrite : this.overwritesOf(member).values()) {
			raw |= overwrite.allow;
			raw &= ~overwrite.deny;
		}
		return new Permission(member, raw);
	}

	/**
	 * @param member
	 * @return
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
