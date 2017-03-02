package io.discloader.discloader.entity;

import java.text.MessageFormat;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.icon.UserIcon;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.util.Constants.Endpoints;

/**
 * Represents a user on discord
 * 
 * @author Perry Berman
 */
public class User {
	/**
	 * The loader instance that cached the user.
	 */
	public final DiscLoader loader;
	/**
	 * The user's unique Snowflake ID.
	 */
	public final String id;

	/**
	 * The user's username
	 */
	public String username;
	/**
	 * The hash of the user's avatar
	 */
	public IIcon avatar;

	// public String avatarURL;
	/**
	 * The user's four digit discriminator
	 */
	public String discriminator;
	/**
	 * Whether or not the user is a bot account
	 */
	public boolean bot;
	/**
	 * Whether or not the user has verified their email address
	 */
	public boolean verified;
	/**
	 * Whethor or not the user has 2FA enabled
	 */
	public boolean mfa;

	public User(DiscLoader loader, UserJSON user) {
		this.loader = loader;

		this.id = user.id;

		if (user.username != null) {
			this.setup(user);
		}
	}

	/**
	 * @param user
	 */
	public User(User user) {
		this.loader = user.loader;

		this.id = user.id;

		this.username = user.username;

		this.discriminator = user.discriminator;

		this.avatar = new UserIcon(this, user.avatar.getIconName());

		// this.avatarURL = this.avatar != null ? Endpoints.avatar(this.id,
		// this.avatar) : null;

		this.bot = user.bot;

		// TextureRegistry.registerUserIcon();
	}

	public void setup(UserJSON data) {
		this.username = data.username;

		this.discriminator = data.discriminator;

		this.avatar = new UserIcon(this, data.avatar);

		// this.avatarURL = this.avatar != null ? Endpoints.avatar(this.id,
		// this.avatar) : null;

		this.bot = data.bot;

//		TextureRegistry.registerUserIcon(new UserIcon(this));

	}

	/**
	 * toStrings the user in mention format
	 * 
	 * @return {@literal <@}{@link #id this.id}{@literal>}
	 */
	public String toString() {
		return MessageFormat.format("<@{0}>", new Object[] { this.id });
	}

	/**
	 * Updates a user's information
	 * 
	 * @param data
	 * @return this
	 */
	public User patch(UserJSON data) {
		if (data.username != null)
			this.username = data.username;

		if (data.discriminator != null)
			this.discriminator = data.discriminator;

		if (data.avatar != null)
			this.avatar = new UserIcon(this, data.avatar);

		if (data.bot == true || data.bot == false)
			this.bot = data.bot;

		// this.avatarURL = this.avatar != null ? Endpoints.avatar(this.id,
		// this.avatar) : null;

		return this;
	}
}
