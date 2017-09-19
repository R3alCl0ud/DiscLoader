package io.discloader.discloader.core.entity.guild;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildEmoji;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.EmojiJSON;

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
		return this.loader.rest.deleteEmoji(this);
	}

	/**
	 * Checks if an emoji is equivalent to {@code this}
	 * 
	 * @param guildEmoji The emoji to check to see if it is the same as {@code this}
	 * @return {@code true} if all fields match, {@code false} otherwise
	 */
	public boolean equals(GuildEmoji guildEmoji) {
		for (IRole role : guildEmoji.getRoles().values()) {
			// no need to continue if roles don't match up
			if (!getRoles().containsKey(role.getID())) return false;
		}
		return id.equals(guildEmoji.id) && guild.getID() == guildEmoji.guild.getID() && name.equals(guildEmoji.name) && managed == guildEmoji.managed;
	}

	@Override
	public Map<String, IRole> getRoles() {
		Map<String, IRole> roles = new HashMap<>();
		for (String r : roleIDs) {
			roles.put(r, guild.getRoles().get(r));
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
		return null;
	}

	@Override
	public CompletableFuture<IGuildEmoji> setRoles(IRole... roles) {
		return null;
	}

}
