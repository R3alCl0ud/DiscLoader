package io.discloader.discloader.entity.channels;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.Permission;
import io.discloader.discloader.entity.Role;
import io.discloader.discloader.entity.impl.IGuildChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants;
import io.discloader.discloader.util.Constants.Permissions;

public class GuildChannel extends Channel implements IGuildChannel {
	public final Guild guild;

	public Permissions permission;

	public int position;
	
	public HashMap<String, Overwrite> overwrites;

	public GuildChannel(Guild guild, ChannelJSON channel) {
		super(guild.loader, channel);

		this.guild = guild;

		this.overwrites = new HashMap<String, Overwrite>();
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.name = data.name;

		this.position = data.position;
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
	 * 
	 * @param member
	 *            The member whose permissions we are evaluating.
	 * @return A new Permissions object that contains {@literal this}, the
	 *         {@literal member}, and their evaluated permissions
	 *         {@link Integer}. <br>
	 *         null if the channel doesn't belong to a {@link #guild}
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
	 * Gets all of the channel's {@link #overwrites} that applies to a
	 * {@link GuildMember}
	 * 
	 * @param member
	 *            The member of whome we are looking for overwrites that apply.
	 * @author Perry Berman
	 * @return A {@link HashMap} of overwrite objects, indexed by
	 *         {@link Overwrite#id}
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

	@Override
	public CompletableFuture<? extends IGuildChannel> setName(String name) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildChannel> setPosition(int position) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildChannel> setPermissions(int allow, int deny, String type) {
		return null;
	}

}
