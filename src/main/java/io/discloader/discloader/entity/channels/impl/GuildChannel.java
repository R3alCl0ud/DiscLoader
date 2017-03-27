package io.discloader.discloader.entity.channels.impl;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.Permission;
import io.discloader.discloader.entity.channels.IGuildChannel;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.Role;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.OverwriteJSON;
import io.discloader.discloader.network.rest.actions.channel.SetOverwrite;
import io.discloader.discloader.network.rest.actions.channel.close.CloseGuildChannel;
import io.discloader.discloader.util.DLUtil;

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
	public final Guild guild;

	/**
	 * The channel's name
	 */
	public String name;

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
	private HashMap<String, Overwrite> overwrites;

	public GuildChannel(Guild guild, ChannelJSON channel) {
		super(guild.loader, channel);

		this.guild = guild;

		overwrites = new HashMap<>();
	}

	public CompletableFuture<? extends GuildChannel> clone() {
		return guild.createChannel(name, type.name());
	}

	@Override
	public CompletableFuture<? extends GuildChannel> delete() {
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
	public CompletableFuture<? extends GuildChannel> edit(String name, String topic, int position, int bitrate, int userLimit) {
		CompletableFuture<VoiceChannel> future = new CompletableFuture<>();
		loader.rest.modifyGuildChannel(this, name, topic, position, bitrate, userLimit).thenAcceptAsync(channel -> {
			future.complete((VoiceChannel) channel);
		});
		return future;
	}

	public HashMap<String, GuildMember> getMembers() {
		HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
		for (GuildMember member : this.guild.members.values()) {
			if (this.permissionsFor(member).hasPermission(DLUtil.PermissionFlags.READ_MESSAGES, false)) members.put(member.id, member);
		}
		return members;
	}

	public HashMap<String, Overwrite> getOverwrites() {
		return overwrites;
	}

	public boolean isPrivate() {
		return false;
	}

	@Override
	public Overwrite overwriteFor(Role role) {
		return overwrites.get(role.id);
	}

	public HashMap<String, Overwrite> overwritesOf(GuildMember member) {
		HashMap<String, Overwrite> Overwrites = new HashMap<String, Overwrite>();
		for (Role role : member.getRoles().values()) {
			if (this.overwrites.get(role.id) != null) Overwrites.put(role.id, this.overwrites.get(role.id));
		}
		if (this.overwrites.get(member.id) != null) Overwrites.put(member.id, this.overwrites.get(member.id));
		return Overwrites;
	}

	public Permission permissionsFor(GuildMember member) {
		int raw = 0;
		if (member.id == this.guild.ownerID) return new Permission(member, this, 2146958463);
		for (Role role : member.getRoles().values())
			raw |= role.getPermissions().asInteger();
		for (Overwrite overwrite : this.overwritesOf(member).values()) {
			raw |= overwrite.allow;
			raw &= ~overwrite.deny;
		}
		return new Permission(member, this, raw);
	}

	@Override
	public CompletableFuture<? extends GuildChannel> setName(String name) {
		return edit(name, null, position, 64000, 0);
	}

	@Override
	public CompletableFuture<Overwrite> setPermissions(int allow, int deny, GuildMember member) {
		return new SetOverwrite(this, new Overwrite(allow, deny, member)).execute();
	}

	@Override
	public CompletableFuture<Overwrite> setPermissions(int allow, int deny, Role role) {
		return new SetOverwrite(this, new Overwrite(allow, deny, role)).execute();
	}

	@Override
	public CompletableFuture<? extends GuildChannel> setPosition(int position) {
		return edit(name, null, position, 64000, 0);
	}

	public void setup(ChannelJSON data) {
		super.setup(data);
		name = data.name;
		position = data.position;

		for (OverwriteJSON ow : data.permission_overwrites) {
			overwrites.put(ow.id, new Overwrite(ow));
		}
	}

}
