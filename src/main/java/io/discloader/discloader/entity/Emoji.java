package io.discloader.discloader.entity;

import java.util.HashMap;

import io.discloader.discloader.network.json.EmojiJSON;

/**
 * @author Perry Berman
 *
 */
public class Emoji {
	public final String id;
	public String name;

	public HashMap<String, Role> roles;

	public boolean requiresColons;

	public final boolean managed;

	public final Guild guild;

	public Emoji(EmojiJSON data, Guild guild) {
		this.id = data.id;
		
		this.name = data.name;

		this.managed = data.managed;

		this.requiresColons = data.requires_colons;

		this.guild = guild;
		
		this.roles = new HashMap<>();
	}

	/**
	 * Returns a string in the correct markdown format for discord emojis
	 * 
	 */
	public String toString() {
		return String.format("<:%s:%s>", this.name, this.id);
	}

}
