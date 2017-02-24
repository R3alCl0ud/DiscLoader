package io.discloader.discloader.entity;

import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.util.Constants;

public class DLUser extends User {

	/**
	 * The currently loggedin user's email address. Only applies to user
	 * accounts
	 */
	public String email;
	/**
	 * The currently loggedin user's password. Only applies to user accounts
	 */
	public String password;

	private boolean afk;

	/**
	 * User's persence. shows as default if {@code user.id != loader.user.id}
	 */
	public final Presence presence;

	public DLUser(User user) {
		super(user);

		this.presence = new Presence();

		this.afk = false;
		
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
	 * @param avatarLocation The location on disk of the new avatar image
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successfull, or the error response if failed. Returns null if
	 *         {@code this.id != this.loader.user.id}
	 */
	public CompletableFuture<User> setAvatar(String avatarLocation) {
		return this.loader.rest.setAvatar(avatarLocation);
	}

	/**
	 * Set's the currently logged in user's presence
	 * 
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
	 * @return 
	 */
	public CompletableFuture<OAuth2Application> getOAuth2Application() {
		return this.loader.rest.getApplicationInfo();
	}

}
