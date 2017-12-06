package io.discloader.discloader.core.entity.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.Game;
import io.discloader.discloader.core.entity.Presence;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.packets.request.PresenceUpdate;
import io.discloader.discloader.network.json.OAuthApplicationJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.ModifyUser;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * Represents the user that you are currently logged in as.
 * 
 * @author Perry Berman
 */
public class DLUser extends User {

	/**
	 * The currently loggedin user's email address. Only applies to user accounts
	 */
	private String email;
	/**
	 * The currently loggedin user's password. Only applies to user accounts
	 */
	private String password;

	private boolean afk;

	/**
	 * User's persence. shows as default if {@code user.id != loader.user.id}
	 */
	private final Presence presence;

	/**
	 * Creates a new DLUser object
	 * 
	 * @param iUser
	 *            The user we are currently logged in as
	 */
	public DLUser(IUser iUser) {
		super(iUser);

		this.presence = new Presence();

		this.afk = false;

	}

	/**
	 * Gets the OAuth2 application of the logged in user, if the {@link User#bot} is
	 * true
	 * 
	 * @return A Future that completes with a new {@link OAuth2Application} if
	 *         successful
	 */
	public CompletableFuture<OAuth2Application> getOAuth2Application() {
		CompletableFuture<OAuth2Application> future = new CompletableFuture<>();
		CompletableFuture<OAuthApplicationJSON> cf = getLoader().rest.request(Methods.GET, Endpoints.currentOAuthApplication, new RESTOptions(), OAuthApplicationJSON.class);
		cf.thenAcceptAsync(appData -> {
			IUser owner = EntityRegistry.addUser(appData.owner);
			future.complete(new OAuth2Application(appData, owner));
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Set's the user's status
	 * 
	 * @param afk
	 *            Whether or not you are afk
	 * @return this
	 */
	public DLUser setAFK(boolean afk) {
		return this.setPresence(null, null, this.afk);
	}

	/**
	 * Sets the currently logged in user's avatar
	 * 
	 * @param avatarLocation
	 *            The location on disk of the new avatar image
	 * @return A CompletableFuture that completes with {@code this} if successfull,
	 *         or the error response if failed. Returns null if
	 *         {@code this.id != this.loader.user.id}
	 */
	public CompletableFuture<DLUser> setAvatar(String avatarLocation) throws IOException {
		CompletableFuture<DLUser> future = new CompletableFuture<>();
		String base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(avatarLocation))));
		CompletableFuture<DLUser> fm = new ModifyUser(this, new JSONObject().put("avatar", base64)).execute();
		fm.handleAsync((dlu, ex) -> {
			if (ex != null)
				future.completeExceptionally(ex.getCause());
			future.complete(dlu);
			return null;
		});
		return future;
	}

	/**
	 * Sets the currently loggedin user's game
	 * 
	 * @param game
	 *            The name of the game you are playing
	 * @return this
	 */
	public DLUser setGame(String game) {
		return setPresence(presence.status, game != null ? new Game(game) : null, this.afk);
	}

	/**
	 * Set's the currently logged in user's presence
	 * 
	 * @param status
	 *            The new status
	 * @param game
	 *            The new game
	 * @param afk
	 *            Are you afk?
	 * @return this
	 */
	public DLUser setPresence(String status, Game game, boolean afk) {
		PresenceUpdate d = new PresenceUpdate(game, status, afk, 0);
		Packet payload = new Packet(3, d);
		presence.update(status, game);
		loader.socket.send(payload);
		return this;
	}

	public void setPresence(IPresence presence) {
		// presence.
		this.presence.update(presence.getStatus(), (Game) presence.getGame());
		// this.presence = (Presence) presence;
	}

	/**
	 * Sets the user's status.
	 * 
	 * @param status
	 *            The new status, can be {@literal online}, {@literal idle},
	 *            {@literal dnd}, {@literal invisible}, etc..
	 * @return this
	 */
	public DLUser setStatus(String status) {
		return this.setPresence(status, null, this.afk);
	}

	/**
	 * Set's the currently logged in user's username.
	 * 
	 * @param username
	 *            The new username for the account
	 * @return A Future that completes with a {@link User} Object if successful
	 */
	public CompletableFuture<DLUser> setUsername(String username) {
		CompletableFuture<DLUser> future = new CompletableFuture<>();
		CompletableFuture<DLUser> fm = new ModifyUser(this, new JSONObject().put("username", username)).execute();
		fm.handleAsync((dlu, ex) -> {
			if (ex != null)
				future.completeExceptionally(ex.getCause());
			future.complete(dlu);
			return null;
		});
		return future;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the presence
	 */
	public Presence getPresence() {
		return this.presence;
	}

}
