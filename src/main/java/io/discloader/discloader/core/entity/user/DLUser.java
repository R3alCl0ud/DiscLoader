package io.discloader.discloader.core.entity.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.presence.Activity;
import io.discloader.discloader.core.entity.presence.Presence;
import io.discloader.discloader.entity.presence.ActivityType;
import io.discloader.discloader.entity.presence.IPresence;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.gateway.packets.request.StatusUpdate;
import io.discloader.discloader.network.json.OAuthApplicationJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.rest.RESTOptions;
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
	 * Creates a new DLUser object.<br>
	 * <br>
	 * This is only to be used internally when receiving the Ready packet from the
	 * gateway.
	 * 
	 * @param user
	 *            The user we are currently logged in as
	 */
	public DLUser(IUser user) {
		super(user);

		this.presence = new Presence();
		this.presence.update("online");
		this.afk = false;

	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DLUser)) {
			if (object instanceof IUser) {
				return super.equals(object); // check
			}
			return false; // if the object isn't and instanceof IUser or DLUser return false.
		}
		return this == (DLUser) object || getID() == ((IUser) object).getID();
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the OAuth2 application of the logged in user, if {@link User#isBot()}
	 * returns true
	 * 
	 * @return A Future that completes with a new {@link OAuth2Application} if
	 *         successful.
	 * @throws AccountTypeException
	 *             Thrown if the account the client is logged in as is a user
	 *             account.
	 * 
	 */
	public CompletableFuture<OAuth2Application> getOAuth2Application() {
		if (!isBot()) {
			throw new AccountTypeException("Cannot fetch the OAuth2Application details of a User Account.");
		}
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the presence
	 */
	public Presence getPresence() {
		return this.presence;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}

	/**
	 * Sets the currently logged in user's activity
	 * 
	 * @param activity
	 *            Your new activity.
	 * @return {@code this} object.
	 */
	public DLUser setActivity(Activity activity) {
		return setPresence(presence.getStatus(), activity, this.afk);
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
		String base64 = "data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(avatarLocation)));
		CompletableFuture<UserJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.currentUser, new RESTOptions(new JSONObject().put("avatar", base64)), UserJSON.class);
		cf.thenAcceptAsync(data -> {
			setup(data);
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Sets the currently logged in user's activity as a game
	 * 
	 * @param game
	 *            The name of the game
	 * @return {@code this} object.
	 */
	public DLUser setGame(String game) {
		return setPresence(presence.getStatus(), game != null ? new Activity(game) : null, this.afk);
	}

	/**
	 * Sets the currently logged in user's activity as listening to music.
	 * 
	 * @param song
	 *            The name of the song you are listening to
	 * @return {@code this} object.
	 */
	public DLUser setListening(String song) {
		return setPresence(presence.getStatus(), song != null ? new Activity(song, ActivityType.LISTENING, null) : null, this.afk);
	}

	public void setPresence(IPresence presence) {
		this.presence.update(presence.getStatus(), (Activity) presence.getActivity());
	}

	/**
	 * Set's the currently logged in user's presence
	 * 
	 * @param status
	 *            The new status
	 * @param activity
	 *            The new activity
	 * @param afk
	 *            Are you afk?
	 * @return this
	 */
	public DLUser setPresence(String status, Activity activity, boolean afk) {
		StatusUpdate d = new StatusUpdate(activity, status, afk, 0);
		Packet payload = new Packet(3, d);
		presence.update(status, activity);
		loader.socket.send(payload);
		return this;
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
	 * Sets the currently logged in user's activity as a live stream.
	 * 
	 * @param name
	 *            The name of the live stream.
	 * @return {@code this} object.
	 */
	public DLUser setStream(String name) {
		Activity activity = new Activity(name, ActivityType.STREAMING, null);
		return setPresence(presence.getStatus(), activity, this.afk);
	}

	/**
	 * Sets the currently logged in user's activity as a live stream
	 * 
	 * @param name
	 *            The name of the live stream.
	 * @param url
	 *            The url of the live stream. Must be a valid twitch.tv live stream
	 *            URL
	 * @return {@code this} object.
	 */
	public DLUser setStream(String name, String url) {
		Activity activity = new Activity(name, ActivityType.STREAMING, url);
		return setPresence(presence.getStatus(), activity, this.afk);
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
		CompletableFuture<UserJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.currentUser, new RESTOptions(new JSONObject().put("username", username)), UserJSON.class);
		cf.thenAcceptAsync(data -> {
			setup(data);
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

}
