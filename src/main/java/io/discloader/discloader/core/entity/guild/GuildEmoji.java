package io.discloader.discloader.core.entity.guild;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.EmojiJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * Represents a guild's custom emoji
 * 
 * @author Perry Berman
 * @since Mar 7, 2017
 * @version 0.1.0
 */
public class GuildEmoji implements IGuildEmoji {

	/**
	 * The guild the Emoji belongs to.
	 */
	private final IGuild guild;

	/**
	 * The Snowflake ID of the emoji
	 */
	private final String id;

	/**
	 * The current instance of DiscLoader
	 */
	private final DiscLoader loader;

	/**
	 * Whether or not the emoji was created by an integration.
	 */
	private final boolean managed;

	/**
	 * The name of the emoji
	 */
	private String name;

	/**
	 * whether this emoji must be wrapped in colons
	 */
	private boolean requiresColons;

	/**
	 * The roles this emoji is active for
	 */
	private String[] roleIDs;

	public GuildEmoji(EmojiJSON data, IGuild guild) {
		id = data.id;

		name = data.name;

		managed = data.managed;

		requiresColons = data.requires_colons;

		this.guild = guild;

		loader = guild.getLoader();

		roleIDs = data.roles;
	}

	/**
	 * Deletes the emoji
	 * 
	 * @return A Future that completes with the deleted emoji if successful.
	 */
	@Override
	public CompletableFuture<IGuildEmoji> delete() {
		CompletableFuture<IGuildEmoji> future = new CompletableFuture<>();
		CompletableFuture<Void> cf = getLoader().rest.request(Methods.DELETE, Endpoints.guildEmoji(getGuild().getID(), getID()), new RESTOptions(), Void.class);
		cf.thenAcceptAsync(Null -> {
			future.complete(GuildEmoji.this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	public boolean equals(Object object) {
		if (!(object instanceof GuildEmoji))
			return false;
		GuildEmoji emoji = (GuildEmoji) object;

		return (this == emoji) || getID() == emoji.getID();

	}

	/**
	 * Checks if an emoji is equivalent to {@code this}
	 * 
	 * @param guildEmoji
	 *            The emoji to check to see if it is the same as {@code this}
	 * @return {@code true} if all fields match, {@code false} otherwise
	 */
	public boolean equals(GuildEmoji guildEmoji) {
		for (IRole role : guildEmoji.getRoles().values()) {
			if (!getRoles().containsKey(role.getID()))
				return false;
		}
		return id.equals(guildEmoji.id) && guild.getID() == guildEmoji.guild.getID() && name.equals(guildEmoji.name) && managed == guildEmoji.managed;
	}

	@Override
	public Map<Long, IRole> getRoles() {
		Map<Long, IRole> roles = new HashMap<>();
		for (String roleID : roleIDs) {
			IRole role = guild.getRoleByID(roleID);
			roles.put(role.getID(), role);
		}
		return roles;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns a string in the correct markdown format for discord emojis
	 */
	@Override
	public String toString() {
		return String.format("<:%s:%s>", this.name, this.id);
	}

	/**
	 * @return the requiresColons
	 */
	public boolean requiresColons() {
		return requiresColons;
	}

	/**
	 * @return the loader
	 */
	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	/**
	 * @return the guild
	 */
	@Override
	public IGuild getGuild() {
		return this.guild;
	}

	@Override
	public long getID() {
		return SnowflakeUtil.parse(id);
	}

	@Override
	public CompletableFuture<IGuildEmoji> setName(String name) {
		if (!guild.hasPermission(Permissions.MANAGE_EMOJIS)) {
			throw new PermissionsException("Insufficient Permissions: \"MANAGE_EMOJIS\" is required to use this endpoint");
		}

		CompletableFuture<IGuildEmoji> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("name", name);
		IEventListener el = new EventListenerAdapter() {
			@Override
			public void GuildEmojiUpdate(GuildEmojiUpdateEvent event) {
				if (getID() == event.getEmoji().getID()) {
					future.complete(event.getEmoji());
					getLoader().removeEventListener(this);
				}
			}
		};
		getLoader().addEventListener(el).rest.request(Methods.PATCH, Endpoints.guildEmoji(getGuild().getID(), getID()), new RESTOptions(payload), EmojiJSON.class).exceptionally(ex -> {
			future.completeExceptionally(ex);
			getLoader().removeEventListener(el);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildEmoji> setRoles(IRole... roles) {
		if (!guild.hasPermission(Permissions.MANAGE_EMOJIS)) {
			throw new PermissionsException("Insufficient Permissions: \"MANAGE_EMOJIS\" is required to use this endpoint");
		}
		CompletableFuture<IGuildEmoji> future = new CompletableFuture<>();
		String[] payload = new String[roles.length];
		for (int i = 0; i < roles.length; i++) {
			payload[i] = SnowflakeUtil.toString(roles[i]);
		}

		IEventListener el = new EventListenerAdapter() {
			@Override
			public void GuildEmojiUpdate(GuildEmojiUpdateEvent event) {
				if (getID() == event.getEmoji().getID()) {
					future.complete(event.getEmoji());
					getLoader().removeEventListener(this);
				}
			}
		};
		getLoader().addEventListener(el).rest.request(Methods.PATCH, Endpoints.guildEmoji(getGuild().getID(), getID()), new RESTOptions(payload), EmojiJSON.class).exceptionally(ex -> {
			future.completeExceptionally(ex);
			getLoader().removeEventListener(el);
			return null;
		});
		return future;
	}

}
