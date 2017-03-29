package io.discloader.discloader.common.entity.user;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.entity.Game;
import io.discloader.discloader.common.entity.Presence;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.SendablePresenceUpdate;

/**
 * Represents the user that you are currently logged in as.
 * 
 * @author Perry Berman
 */
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

	/**
	 * Creates a new DLUser object
	 * 
	 * @param user The user we are currently logged in as
	 */
	public DLUser(User user) {
		super(user);

		this.presence = new Presence();

		this.afk = false;

	}

	/**
	 * Gets the OAuth2 application of the logged in user, if the
	 * {@link User#bot} is true
	 * 
	 * @return A Future that completes with a new {@link OAuth2Application} if
	 *         successful
	 */
	public CompletableFuture<OAuth2Application> getOAuth2Application() {
		return this.loader.rest.getApplicationInfo();
	}

	/**
	 * Set's the user's status
	 * 
	 * @param afk Whether or not you are afk
	 * @return this
	 */
	public DLUser setAFK(boolean afk) {
		return this.setPresence(null, null, this.afk);
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
	 * Sets the currently loggedin user's game
	 * 
	 * @param game The name of the game you are playing
	 * @return this
	 */
	public DLUser setGame(String game) {
		return this.setPresence(presence.status, game != null ? new Game(game) : null, this.afk);
	}

	/**
	 * Set's the currently logged in user's presence
	 * 
	 * @param status The new status
	 * @param game The new game
	 * @param afk Are you afk?
	 * @return this
	 */
	public DLUser setPresence(String status, Game game, boolean afk) {
		SendablePresenceUpdate d = new SendablePresenceUpdate(game, status, afk, 0);
		Packet payload = new Packet(3, d);
		presence.update(status, game);
		loader.socket.send(payload);
		return this;
	}

	/**
	 * Sets the user's status.
	 * 
	 * @param status The new status, can be {@literal online}, {@literal idle},
	 *            {@literal dnd}, {@literal invisible}, etc..
	 * @return this
	 */
	public DLUser setStatus(String status) {
		return this.setPresence(status, null, this.afk);
	}

	/**
	 * Set's the currently logged in user's username.
	 * 
	 * @param username The new username for the account
	 * @return A Future that completes with a {@link User} Object if successful
	 */
	public CompletableFuture<User> setUsername(String username) {
		return this.loader.rest.setUsername(username);
	}

}
