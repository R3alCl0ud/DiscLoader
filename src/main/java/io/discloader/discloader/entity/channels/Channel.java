package io.discloader.discloader.entity.channels;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.User;
import io.discloader.discloader.entity.impl.IChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants.ChannelType;

public class Channel implements IChannel {

	public String id;

	public String name;

	protected ChannelType type;

	/**
	 * Whether or not the channel is a dm channel. Is always {@literal true} if
	 * {@link #type} is {@literal "groupDM"} or {@literal "dm"}
	 * 
	 * @author Perry Berman
	 */
	public boolean isPrivate;

	public final DiscLoader loader;

	public User user;

	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by
	 * {@link User#id}. <br>
	 * Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, User> recipients;

	/**
	 * A {@link HashMap} of the channel's {@link Overwrite overwrites}. Indexed
	 * by {@link Overwrite#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, Overwrite> overwrites;

	/**
	 * A {@link HashMap} of the channel's {@link GuildMember members}. Indexed
	 * by {@link GuildMember #id member.id}. <br>
	 * Is {@code null} if {@link #guild} is {@code null}, and if {@link #type}
	 * is {@code "dm"}, or {@code "groupDM"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, GuildMember> members;

	public Channel(DiscLoader loader, ChannelJSON data) {
		this.loader = loader;

		this.type = null;

		this.recipients = new HashMap<String, User>();

		this.members = null;

		this.overwrites = new HashMap<String, Overwrite>();

		if (data != null)
			this.setup(data);
	}

	public Channel(Guild guild, ChannelJSON data) {
		this.loader = guild.loader;

		this.type = null;

		this.recipients = null;

		this.members = new HashMap<String, GuildMember>();

		this.overwrites = new HashMap<String, Overwrite>();

		if (data != null)
			this.setup(data);
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public ChannelType getType() {
		return this.type;
	}

	@Override
	public boolean isPrivate() {
		return false;
	}

	public void setup(ChannelJSON data) {
		this.id = data.id;

		this.isPrivate = data.is_private;

		if (data.name != null)
			this.name = data.name;

	}
}
