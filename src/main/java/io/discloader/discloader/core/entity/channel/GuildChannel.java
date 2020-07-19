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
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.Overwrite;
import io.discloader.discloader.core.entity.guild.Permission;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.RestAction;
import io.discloader.discloader.network.rest.actions.channel.CreateInvite;
import io.discloader.discloader.network.rest.actions.channel.FetchInvites;
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

	private List<IInvite> invites = new ArrayList<>();

	public GuildChannel(IGuild guild, ChannelJSON channel) {
		super(guild.getLoader(), channel);

		this.guild = guild;

		overwrites = new HashMap<>();
	}

	@Override
	public RestAction<IInvite> createInvite() {
		return createInvite(86400, 0, false, false);
	}

	@Override
	public RestAction<IInvite> createInvite(boolean temporaryMembership, boolean unique) {
		return createInvite(86400, 0, temporaryMembership, unique);
	}

	@Override
	public RestAction<IInvite> createInvite(int expiresIn, int maxUses) {
		return createInvite(expiresIn, maxUses, false, false);
	}

	@Override
	public RestAction<IInvite> createInvite(int expiresIn, int maxUses, boolean temporaryMembership, boolean unique) {
		return new CreateInvite(this, new JSONObject().put("max_age", expiresIn).put("max_uses", maxUses)
				.put("temporary", temporaryMembership).put("unique", unique));
	}

	@Override
	public CompletableFuture<IGuildChannel> delete() {
		return new CloseGuildChannel(this).execute();
	}

	@Override
	public CompletableFuture<IOverwrite> deleteOverwrite(IOverwrite overwrite) {
		CompletableFuture<IOverwrite> future = new CompletableFuture<>();
		loader.rest.request(Methods.DELETE, Endpoints.channelOverwrite(getID(), overwrite.getID()), new RESTOptions(),
				Void.class).thenAcceptAsync(n -> {
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
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(props), ChannelJSON.class)
				.thenAcceptAsync(data -> {
					future.complete(removed);
				}).exceptionally(ex -> {
					future.completeExceptionally(ex);
					return null;
				});
		return future;
	}

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
	public CompletableFuture<? extends IGuildChannel> edit(String name, int position, boolean nsfw,
			IOverwrite... overwrites) throws PermissionsException {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("name", name).put("position", position).put("nsfw", nsfw)
				.put("permission_overwrites", overwrites);
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(settings), ChannelJSON.class)
				.thenAcceptAsync(data -> {
					IChannel newChannel = EntityBuilder.getChannelFactory().buildChannel(data, getLoader(), getGuild(),
							false);
					if (newChannel instanceof IGuildChannel)
						future.complete((IGuildChannel) newChannel);
				}).exceptionally(ex -> {
					future.completeExceptionally(ex);
					return null;
				});
		return future;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GuildChannel))
			return false;
		GuildChannel chan = (GuildChannel) obj;
		return (this == chan) || (getID() == chan.getID());
	}

	@Override
	public RestAction<List<IInvite>> fetchInvites() {
		return new FetchInvites(this).onSuccess(invts -> {
			for (IInvite invite : invts) {
				if (getInviteByCode(invite.getCode()) == null) {
					invites.add(invite);
				}
			}
		});
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
	public IInvite getInviteByCode(String code) {
		for (IInvite invite : getInvites()) {
			if (invite.getCode().equals(code)) {
				return invite;
			}
		}
		return null;
	}

	@Override
	public List<IInvite> getInvites() {
		return invites;
	}

	@Override
	public List<IInvite> getInvitesByInviter(IGuildMember member) {
		return getInvitesByInviter(member.getUser());
	}

	@Override
	public List<IInvite> getInvitesByInviter(IUser user) {
		return getInvitesByInviter(user.getID());
	}

	public List<IInvite> getInvitesByInviter(long userID) {
		List<IInvite> invites = new ArrayList<>();
		for (IInvite invite : getInvites()) {
			if (invite.getInviter().getID() == userID) {
				invites.add(invite);
			}
		}
		return invites;
	}

	public List<IInvite> getInvitesByInviter(String userID) {
		return getInvitesByInviter(SnowflakeUtil.parse(userID));
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

	@Override
	public IOverwrite getOverwriteByID(long id) {
		return getOverwrites().get(id);
	}

	@Override
	public IOverwrite getOverwriteByID(String id) {
		return getOverwriteByID(SnowflakeUtil.parse(id));
	}

	public Map<Long, IOverwrite> getOverwrites() {
		return overwrites;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
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
	public IOverwrite overwriteOf(IGuildMember member) {
		return getOverwriteByID(member.getID());
	}

	@Override
	public IOverwrite overwriteOf(IRole role) {
		return getOverwriteByID(role.getID());
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
			raw &= ~overwrite.getDenied();
			raw |= overwrite.getAllowed();
		}
		return new Permission(member, this, raw);
	}

	@Override
	public IPermission permissionsOf(IRole role) {
		long raw = role.getPermissions().toLong();
		raw &= ~overwriteOf(role).getDenied();
		raw |= overwriteOf(role).getAllowed();
		return new Permission(role, raw, this);
	}

	protected String sanitizeChannelName(String name) {
		return name.toLowerCase().replace(' ', '-');
	}

	@Override
	public CompletableFuture<IGuildChannel> setCategory(IChannelCategory category) {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("parent_id", SnowflakeUtil.toString(category));
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(settings), ChannelJSON.class)
				.thenAcceptAsync(data -> {
					IGuildChannel newChannel = (IGuildChannel) EntityBuilder.getChannelFactory().buildChannel(data,
							getLoader(), getGuild(), false);
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
		if (overwrite.getType().equals("member")) {
			return setPermissions(overwrite.getAllowed(), overwrite.getDenied(), overwrite.getMember());
		}

		return setPermissions(overwrite.getAllowed(), overwrite.getDenied(), overwrite.getRole());
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
	public CompletableFuture<IOverwrite> setOverwrite(IOverwrite overwrite, String reason) {
		if (overwrite.getType().equals("member")) {
			return setPermissions(overwrite.getAllowed(), overwrite.getDenied(), overwrite.getMember(), reason);
		}
		return setPermissions(overwrite.getAllowed(), overwrite.getDenied(), overwrite.getRole(), reason);
	}

	@Override
	public CompletableFuture<List<IOverwrite>> setOverwrite(String reason, IOverwrite... overwrites)
			throws PermissionsException {
		CompletableFuture<List<IOverwrite>> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("name", name).put("position", position).put("nsfw", nsfw)
				.put("permission_overwrites", overwrites);
		loader.rest.request(Methods.PATCH, Endpoints.channel(getID()), new RESTOptions(true, settings, reason),
				ChannelJSON.class).thenAcceptAsync(data -> {
					IChannel newChannel = EntityBuilder.getChannelFactory().buildChannel(data, getLoader(), getGuild(),
							false);
					if (newChannel instanceof IGuildChannel) {
						future.complete(new ArrayList<>(((IGuildChannel) newChannel).getOverwrites().values()));
					}
				}).exceptionally(ex -> {
					future.completeExceptionally(ex);
					return null;
				});
		return future;
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(long allow, long deny, IGuildMember member) {
		CompletableFuture<IOverwrite> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("allow", allow).put("deny", deny).put("type", "member");
		CompletableFuture<Void> cf = loader.rest.request(Methods.PUT,
				Endpoints.channelOverwrite(getID(), member.getID()), new RESTOptions(payload), Void.class);
		cf.thenAcceptAsync(V -> {
			future.complete(new Overwrite(allow, deny, member));
		});
		cf.exceptionally((ex) -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(long allow, long deny, IGuildMember member, String reason) {
		CompletableFuture<IOverwrite> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("allow", allow).put("deny", deny).put("type", "member");
		CompletableFuture<Void> cf = loader.rest.request(Methods.PUT,
				Endpoints.channelOverwrite(getID(), member.getID()), new RESTOptions(true, payload, reason),
				Void.class);
		cf.thenAcceptAsync(V -> {
			future.complete(new Overwrite(allow, deny, member));
		});
		cf.exceptionally((ex) -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(long allow, long deny, IRole role) {
		CompletableFuture<IOverwrite> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("allow", allow).put("deny", deny).put("type", "role");
		CompletableFuture<Void> cf = loader.rest.request(Methods.PUT, Endpoints.channelOverwrite(getID(), role.getID()),
				new RESTOptions(payload), Void.class);
		cf.thenAcceptAsync(V -> {
			future.complete(new Overwrite(allow, deny, role));
		});
		cf.exceptionally((ex) -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IOverwrite> setPermissions(long allow, long deny, IRole role, String reason) {
		CompletableFuture<IOverwrite> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("allow", allow).put("deny", deny).put("type", "role");
		CompletableFuture<Void> cf = loader.rest.request(Methods.PUT, Endpoints.channelOverwrite(getID(), role.getID()),
				new RESTOptions(true, payload, reason), Void.class);
		cf.thenAcceptAsync(V -> {
			future.complete(new Overwrite(allow, deny, role));
		});
		cf.exceptionally((ex) -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
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
				positions.add(new JSONObject().put("id", SnowflakeUtil.toString(channel)).put("position", position));
			} else if (channel.getPosition() >= position) {
				positions.add(new JSONObject().put("id", SnowflakeUtil.toString(channel)).put("position",
						channel.getPosition() + 1));
			} else {
				positions.add(new JSONObject().put("id", SnowflakeUtil.toString(channel)).put("position",
						channel.getPosition()));
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
		getLoader().rest.request(Methods.PATCH, Endpoints.guildChannels(getGuild().getID()),
				new RESTOptions(true, positions.toString()), Void.class).exceptionally(ex -> {
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
		if (data.parent_id != null) {
			parentID = SnowflakeUtil.parse(data.parent_id);
		}
	}

	@Override
	public String toString() {
		return getName();
	}

}
