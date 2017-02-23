package io.discloader.discloader.entity;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.icon.UserIcon;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.gateway.json.UserJSON;
import io.discloader.discloader.util.Constants;
import io.discloader.discloader.util.Constants.Endpoints;

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
	 * The currently loggedin user's email address. Only applies to user
	 * accounts
	 */
	public String email;
	/**
	 * The currently loggedin user's password. Only applies to user accounts
	 */
	public String password;
	/**
	 * The user's username
	 */
	public String username;
	/**
	 * The hash of the user's avatar
	 */
	public String avatar;
	
	
	public String avatarURL;
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
	
	
	private boolean afk;

	/**
	 * User's persence. shows as default if {@code user.id != loader.user.id}
	 */
	public final Presence presence;

	public User(DiscLoader loader, UserJSON user) {
		this.loader = loader;

		this.id = user.id;

		this.presence = new Presence();

		this.afk = false;

		if (user.username != null) {
			this.setup(user);
		}
	}

	/**
	 * @param user
	 */
	public User(DiscLoader loader, User user) {
		this.loader = loader;

		this.id = user.id;

		this.username = user.username;

		this.discriminator = user.discriminator;

		this.password = user.password;

		this.avatar = user.avatar;
		
		this.avatarURL = this.avatar != null ? Endpoints.avatar(this.id, this.avatar) : null;

		this.bot = user.bot;

		this.presence = new Presence();

		this.afk = false;
		
		TextureRegistry.registerUserIcon(new UserIcon(this));
	}

	public void setup(UserJSON data) {
		this.username = data.username;

		this.discriminator = data.discriminator;

		this.password = data.password;

		this.avatar = data.avatar;

		this.avatarURL = this.avatar != null ? Endpoints.avatar(this.id, this.avatar) : null;
		
		this.bot = data.bot;
		
		TextureRegistry.registerUserIcon(new UserIcon(this));
		
	}

	/**
	 * toStrings the user in mention format
	 * 
	 * @return <@ this.id >
	 */
	public String toString() {
		return MessageFormat.format("<@{0}>", new Object[] { this.id });
	}

	/**
	 * Set's the currently logged in user's username.
	 * 
	 * @param username
	 * @return CompletableFuture
	 */
	public CompletableFuture<User> setUsername(String username) {
		return this.loader.rest.setUsername(username);
	}

	/**
	 * Sets the currently logged in user's avatar
	 * 
	 * @param avatarLocation
	 *            The location on disk of the new avatar image
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successfull, or the error response if failed. Returns null if
	 *         {@code this.id != this.loader.user.id}
	 */
	public CompletableFuture<User> setAvatar(String avatarLocation) {
		return this.loader.rest.setAvatar(avatarLocation);
	}

	/**
	 * Set's the currently logged in user's presence
	 * @param status
	 * @param game
	 * @param afk
	 * @return this
	 */
	public User setPresence(String status, Game game, boolean afk) {
		JSONObject payload = new JSONObject().put("op", Constants.OPCodes.STATUS_UPDATE);
		game = game != null ? game : this.presence.game;
		JSONObject d = new JSONObject().put("game", game != null ? game.toJsonString() : null).put("afk", this.afk)
				.put("status", status != null ? status : this.presence.status).put("since", 0);
		this.presence.update(status, game);
		this.loader.discSocket.send(payload.put("d", d));
		return this;
	}

	/**
	 * Sets the currently loggedin user's game
	 * 
	 * @param game
	 * @return this
	 */
	public User setGame(String game) {
		return this.setPresence(null, game != null ? new Game(game) : null, this.afk);
	}

	/**
	 * Sets the user's status.
	 * 
	 * @param status
	 * @return this
	 */
	public User setStatus(String status) {
		return this.setPresence(status, null, this.afk);
	}

	/**
	 * 
	 * @param afk
	 * @return this
	 */
	public User setAFK(boolean afk) {
		return this.setPresence(null, null, this.afk);
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

		if (data.password != null)
			this.password = data.password;

		if (data.avatar != null)
			this.avatar = data.avatar;

		if (data.bot == true || data.bot == false)
			this.bot = data.bot;
		
		this.avatarURL = this.avatar != null ? Endpoints.avatar(this.id, this.avatar) : null;

		return this;
	}
}
