package io.discloader.discloader.entity.guild;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.json.EmojiJSON;

/**
 * Represents a guild's custom emoji
 * 
 * @author Perry Berman
 * @since 0.0.3
 */
public class Emoji {

	/**
	 * The guild the Emoji belongs to.
	 */
	public final Guild guild;

	/**
	 * The Snowflake ID of the emoji
	 */
	public final String id;

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;

	/**
	 * Whether or not the emoji was created by an integration.
	 */
	public final boolean managed;

	/**
	 * The name of the emoji
	 */
	public String name;

	/**
	 * whether this emoji must be wrapped in colons
	 */
	public boolean requiresColons;

	/**
	 * The roles this emoji is active for
	 */
	public HashMap<String, Role> roles;

	public Emoji(EmojiJSON data, Guild guild) {
		this.id = data.id;

		this.name = data.name;

		this.managed = data.managed;

		this.requiresColons = data.requires_colons;

		this.guild = guild;

		this.loader = guild.loader;

		this.roles = new HashMap<>();

		for (String r : data.roles) {
			this.roles.put(r, guild.roles.get(r));
		}
	}

	/**
	 * Deletes the emoji
	 * 
	 * @return A Future that completes with the deleted emoji if successful.
	 */
	public CompletableFuture<Emoji> delete() {
		return this.loader.rest.deleteEmoji(this);
	}

	/**
	 * Returns a string in the correct markdown format for discord emojis
	 * 
	 */
	public String toString() {
		return String.format("<:%s:%s>", this.name, this.id);
	}

}
