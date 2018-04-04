package io.discloader.discloader.core.entity.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.channel.GuildChannelUpdateEvent;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.Overwrite;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.channel.FetchInvites;
import io.discloader.discloader.network.rest.actions.channel.SetOverwrite;
import io.discloader.discloader.network.rest.actions.channel.close.CloseGuildChannel;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * Represents any channel in a guild
 * 
 * @author Perry Berman
 */
public class GuildChannel extends Channel implements IGuildChannel {

	/**
	 * The {@link Guild} the channel belongs to. <br>
	 * This property <u>must</u> be {@code null} if the {@link #type} of the channel
	 * is {@code "dm"}, or {@code "groupDM"}.
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
	 * A {@link HashMap} of the channel's {@link Overwrite overwrites}. Indexed by
	 * {@link Overwrite#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private Map<Long, IOverwrite> overwrites;

	private boolean nsfw;

	private long parentID;

	public GuildChannel(IGuild guild, ChannelJSON channel) {
		super(guild.getLoader(), channel);

		this.guild = guild;

		overwrites = new HashMap<>();
	}

	@Override
	public CompletableFuture<IGuildChannel> delete() {
		return new CloseGuildChannel(this).execute();
	}

	// @Override
	// public CompletableFuture<IGuildChannel> clone() {
	// return guild.createTextChannel(name);
	// }

	@Override
	public CompletableFuture<IOverwrite> deleteOverwrite(IOverwrite overwrite) {
		CompletableFuture<IOverwrite> future = new CompletableFuture<>();
		loader.rest.request(Methods.DELETE, Endpoints.channelOverwrite(getID(), overwrite.getID()), new RESTOptions(), Void.class).thenAcceptAsync(n -> {
			future.complete(overwrite);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<List<IOverwrite>> deleteOverwrites(IOverwrite... overwrites) {
		CompletableFuture<List<IOverwrite>> future = new CompletableFuture<>();
		List<IOverwrite> keep = new ArrayList<>(), removed = new ArrayList<>();
		for (IOverwrite ow : this.overwrites.values())
			keep.add(ow);
		for (IOverwrite ow : overwrites) {
			if (this.overwrites.containsKey(ow.getID())) {
				keep.remove(this.overwrites.get(ow.getID()));
				removed.add(this.overwrites.get(ow.getID()));
			}
		}

		JSONObject props = new JSONObject().put("permission_overwrites", keep);
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(props), ChannelJSON.class).thenAcceptAsync(data -> {
			future.complete(removed);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	// /**
	// * Changes the channels settings
	// *
	// * @param name The new name for the channel
	// * @param topic The new topic for the channel
	// * @param position The new position for the channel
	// * @param bitrate The new bitrate
	// * @param userLimit The new userLimit
	// * @return A Future that completes with an IGuildChannel if successful
	// */
	//// @Override
	// public CompletableFuture<IGuildChannel> edit(String name, String topic,
	// int position, int bitrate, int userLimit) {
	// CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
	// loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new
	// RESTOptions, cls)
	//// loader.rest.modifyGuildChannel(this, name, topic, position, bitrate,
	// userLimit).thenAcceptAsync(channel -> {
	//// future.complete(channel);
	//// });
	// return future;
	// }

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(int position, boolean nsfw) {
		return edit(name, position, nsfw);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, boolean nsfw) {
		return edit(name, position, nsfw);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, int position) {
		return edit(name, position, nsfw);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, int position, boolean nsfw) {
		return edit(name, position, nsfw, overwrites.values().toArray(new IOverwrite[0]));
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> edit(String name, int position, boolean nsfw, IOverwrite... overwrites) throws PermissionsException {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("name", name).put("position", position).put("nsfw", nsfw).put("permission_overwrites", overwrites);
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(settings), ChannelJSON.class).thenAcceptAsync(data -> {
			IChannel newChannel = EntityBuilder.getChannelFactory().buildChannel(data, getLoader(), getGuild(), false);
			if (newChannel instanceof IGuildChannel)
				future.complete((IGuildChannel) newChannel);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public IChannelCategory getCategory() {
		return getGuild().getChannelCategoryByID(parentID);
	}

	@Override
	public IGuild getGuild() {
		return EntityRegistry.getGuildByID(guild.getID());
	}

	@Override
	public CompletableFuture<List<IInvite>> getInvites() {
		return new FetchInvites(this).execute();
	}

	@Override
	public Map<Long, IGuildMember> getMembers() {
		Map<Long, IGuildMember> members = new HashMap<>();
		for (IGuildMember member : getGuild().getMembers().values()) {
			if (permissionsOf(member).hasPermission(Permissions.READ_MESSAGES, false))
				members.put(member.getID(), member);
		}
		return members;
	}

	@Override
	public String getName() {
		return name;
	}

	public Map<Long, IOverwrite> getOverwrites() {
		return overwrites;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public boolean inCategory() {
		return getCategory() != null;
	}

	@Override
	public boolean inCategory(IChannelCategory category) {
		return inCategory() && getCategory().getID() == category.getID();
	}

	@Override
	public boolean isNSFW() {
		return nsfw;
	}

	@Override
	public boolean isPrivate() {
		return false;
	}

	@Override
	public IOverwrite overwriteOf(IRole role) {
		return overwrites.get(role.getID());
	}

	@Override
	public List<IOverwrite> overwritesOf(IGuildMember member) {
		List<IOverwrite> Overwrites = new ArrayList<>();
		for (IRole role : member.getRoles()) {
			if (role != null && overwrites.get(role.getID()) != null)
				Overwrites.add(overwrites.get(role.getID()));
		}
		if (overwrites.get(member.getID()) != null)
			Overwrites.add(overwrites.get(member.getID()));
		return Overwrites;
	}

	@Override
	public IPermission permissionsOf(IGuildMember member) {
		long raw = 0;
		if (getGuild().isOwner(member))
			return new Permission(member, this, 2146958463);
		for (IRole role : member.getRoles()) {
			if (role != null) {
				raw |= role.getPermissions().toLong();
			}
		}
		for (IOverwrite overwrite : overwritesOf(member)) {
			raw |= overwrite.getAllowed();
			raw &= ~overwrite.getDenied();
		}
		return new Permission(member, this, raw);
	}

	protected String sanitizeChannelName(String name) {
		return name.toLowerCase().replace(' ', '-');
	}

	@Override
	public CompletableFuture<IGuildChannel> setCategory(IChannelCategory category) {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("parent_id", SnowflakeUtil.asString(category));
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(settings), ChannelJSON.class).thenAcceptAsync(data -> {
			IGuildChannel newChannel = (IGuildChannel) EntityBuilder.getChannelFactory().buildChannel(data, getLoader(), getGuild(), false);
			future.complete(newChannel);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> setName(String name) {
		return edit(name, position);
	}

	@Override
	public CompletableFuture<? extends IGuildChannel> setNSFW(boolean nswf) {
		return edit(name, position, nsfw);
	}

	@Override
	public CompletableFuture<IOverwrite> setOverwrite(IOverwrite overwrite) {
		return new SetOverwrite(this, overwrite).execute();
	}

	@Override
	public CompletableFuture<List<IOverwrite>> setOverwrite(IOverwrite... overwrites) {
		CompletableFuture<List<IOverwrite>> future = new CompletableFuture<>();
		CompletableFuture<? extends IGuildChannel> cf = edit(name, position, nsfw, overwrites);
		cf.thenAcceptAsync(channel -> {
			future.complete(new ArrayList<>(channel.getOverwrites().values()));
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
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
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		List<JSONObject> positions = new ArrayList<>();
		boolean normalize = position < 0;
		List<IGuildChannel> channels;
		switch (getType()) {
		case CATEGORY:
			channels = new ArrayList<>(getGuild().getChannelCategories().values());
			break;
		case TEXT:
			channels = new ArrayList<>(getGuild().getTextChannels().values());
			break;
		case VOICE:
			channels = new ArrayList<>(getGuild().getVoiceChannels().values());
			break;
		default:
			channels = new ArrayList<>();
			break;
		}
		for (IGuildChannel channel : channels) {
			if (channel.getID() == getID()) {
				positions.add(new JSONObject().put("id", SnowflakeUtil.asString(channel)).put("position", position));
			} else if (channel.getPosition() >= position) {
				positions.add(new JSONObject().put("id", SnowflakeUtil.asString(channel)).put("position", channel.getPosition() + 1));
			} else {
				positions.add(new JSONObject().put("id", SnowflakeUtil.asString(channel)).put("position", channel.getPosition()));
			}
			if (channel.getPosition() < 0)
				normalize = true;
		}
		positions.sort((a, b) -> {
			if (a.getInt("position") < b.getInt("position"))
				return -1;
			if (a.getInt("position") > b.getInt("position"))
				return 1;
			return 0;
		});
		if (normalize) {
			for (int i = 0; i < positions.size(); i++) {
				positions.get(i).put("position", i);
			}
		}

		getLoader().addEventListener(new EventListenerAdapter() {
			@Override
			public void GuildChannelUpdate(GuildChannelUpdateEvent e) {
				if (getID() == e.getChannel().getID()) {
					future.complete(e.getChannel());
					getLoader().removeEventListener(this);
				}
			}
		});
		getLoader().rest.request(Methods.PATCH, Endpoints.guildChannels(getGuild().getID()), new RESTOptions(true, positions.toString()), Void.class).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});

		return future;
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);
		name = data.name;
		position = data.position;

	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GuildChannel))
			return false;
		GuildChannel chan = (GuildChannel) obj;
		return (this == chan) || (getID() == chan.getID());
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}

}
