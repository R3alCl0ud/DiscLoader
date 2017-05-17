package io.discloader.discloader.core.entity.channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.Overwrite;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.channel.FetchInvites;
import io.discloader.discloader.network.rest.actions.channel.SetOverwrite;
import io.discloader.discloader.network.rest.actions.channel.close.CloseGuildChannel;

/**
 * Represents any channel in a guild
 * 
 * @author Perry Berman
 */
public class GuildChannel extends Channel implements IGuildChannel {

	/**
	 * The {@link Guild} the channel belongs to. <br>
	 * This property <u>must</u> be {@code null} if the {@link #type} of the
	 * channel is {@code "dm"}, or {@code "groupDM"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	protected final IGuild guild;

	/**
	 * The channel's name
	 */
	protected String name;

	/**
	 * The channel's position
	 */
	public int position;

	/**
	 * A {@link HashMap} of the channel's {@link Overwrite overwrites}. Indexed
	 * by {@link Overwrite#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private HashMap<Long, IOverwrite> overwrites;

	public GuildChannel(IGuild guild, ChannelJSON channel) {
		super(guild.getLoader(), channel);

		this.guild = guild;

		overwrites = new HashMap<>();
	}

	// @Override
	// public CompletableFuture<IGuildChannel> clone() {
	// return guild.createTextChannel(name);
	// }

	@Override
	public CompletableFuture<IGuildChannel> delete() {
		return new CloseGuildChannel(this).execute();
	}

	/**
	 * Changes the channels settings
	 * 
	 * @param name The new name for the channel
	 * @param topic The new topic for the channel
	 * @param position The new position for the channel
	 * @param bitrate The new bitrate
	 * @param userLimit The new userLimit
	 * @return A Future that completes with an IGuildChannel if successful
	 */
	@Override
	public CompletableFuture<IGuildChannel> edit(String name, String topic, int position, int bitrate, int userLimit) {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		loader.rest.modifyGuildChannel(this, name, topic, position, bitrate, userLimit).thenAcceptAsync(channel -> {
			future.complete(channel);
		});
		return future;
	}

	@Override
	public HashMap<Long, IGuildMember> getMembers() {
		HashMap<Long, IGuildMember> members = new HashMap<>();
		for (IGuildMember member : guild.getMembers().values()) {
			if (this.permissionsFor(member).hasPermission(Permissions.READ_MESSAGES, false)) members.put(member.getID(), member);
		}
		return members;
	}

	public HashMap<Long, IOverwrite> getOverwrites() {
		return overwrites;
	}

	@Override
	public boolean isPrivate() {
		return false;
	}

	@Override
	public IOverwrite overwriteFor(IRole role) {
		return overwrites.get(role.getID());
	}

	@Override
	public Collection<IOverwrite> overwritesOf(IGuildMember member) {
		HashMap<Long, IOverwrite> Overwrites = new HashMap<>();
		for (IRole role : member.getRoles().values()) {
			if (role != null && overwrites.get(role.getID()) != null) Overwrites.put(role.getID(), overwrites.get(role.getID()));
		}
		if (overwrites.get(member.getID()) != null) Overwrites.put(member.getID(), overwrites.get(member.getID()));
		return Overwrites.values();
	}

	@Override
	public IPermission permissionsFor(IGuildMember member) {
		int raw = 0;
		if (guild.isOwner(member)) return new Permission(member, this, 2146958463);
		for (IRole role : member.getRoles().values()) {
			if (role != null) raw |= role.getPermissions().asInt();
		}
		for (IOverwrite overwrite : overwritesOf(member)) {
			raw |= overwrite.getAllowed();
			raw &= ~overwrite.getDenied();
		}
		return new Permission(member, this, raw);
	}

	@Override
	public CompletableFuture<IGuildChannel> setName(String name) {
		return edit(name, null, position, 64000, 0);
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(int allow, int deny, IGuildMember member) {
		return new SetOverwrite(this, new Overwrite(allow, deny, member)).execute();
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(int allow, int deny, Role role) {
		return new SetOverwrite(this, new Overwrite(allow, deny, role)).execute();
	}

	@Override
	public CompletableFuture<IGuildChannel> setPosition(int position) {
		return edit(name, null, position, 64000, 0);
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);
		name = data.name;
		position = data.position;
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public CompletableFuture<IOverwrite> setOverwrite(IOverwrite overwrite) {
		return new SetOverwrite(this, overwrite).execute();
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public CompletableFuture<List<IInvite>> getInvites() {
		return new FetchInvites(this).execute();
	}

}
