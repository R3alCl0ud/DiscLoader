package io.disc.discloader.objects.structures;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.objects.gateway.UserJSON;
import io.disc.discloader.util.constants;

public class User {
	public final DiscLoader loader;
	public final String id;
	public String email;
	public String password;
	public String username;
	public String avatar;
	public String discriminator;
	public boolean bot;
	public boolean verified;
	public boolean mfa;
	private boolean afk;

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

		this.bot = user.bot;

		this.presence = new Presence();
		
		this.afk = false;
	}

	public void setup(UserJSON data) {
		this.username = data.username;

		this.discriminator = data.discriminator;

		this.password = data.password;

		this.avatar = data.avatar;

		this.bot = data.bot;
	}

	public String toString() {
		return MessageFormat.format("<@{0}>", new Object[] { this.id });
	}

	/**
	 * @param username
	 * @return CompletableFuture
	 */
	public CompletableFuture<User> setUsername(String username) {
		return this.loader.rest.setUsername(username);
	}

	/**
	 * @param status
	 * @param game
	 * @param afk
	 * @return this
	 */
	public User setPresence(String status, Game game, boolean afk) {
		JSONObject payload = new JSONObject().put("op", constants.OPCodes.STATUS_UPDATE);
		game = game != null ? game : this.presence.game;
		JSONObject d = new JSONObject().put("game", game != null ? game.toJsonString() : null).put("afk", this.afk)
				.put("status", status != null ? status : this.presence.status).put("since", 0);
		this.presence.update(status, game);
		this.loader.discSocket.send(payload.put("d", d));
		return this;
	}

	/**
	 * @param game
	 * @return this
	 */
	public User setGame(String game) {
		return this.setPresence(null, game != null ? new Game(game) : null, this.afk);
	}
	
	/**
	 * @param status
	 * @return this
	 */
	public User setStatus(String status) {
		return this.setPresence(status, null, this.afk);
	}
	
	/**
	 * @param afk
	 * @return this
	 */
	public User setAFK(boolean afk) {
		return this.setPresence(null, null, this.afk);
	}

	/**
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

		if (data.password != null)
			this.avatar = data.avatar;

		if (data.bot == true || data.bot == false)
			this.bot = data.bot;

		return this;
	}
}
