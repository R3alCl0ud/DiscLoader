package io.discloader.discloader.core.entity;

import io.discloader.discloader.entity.IGame;
import io.discloader.discloader.entity.IPresence;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.GameJSON;
import io.discloader.discloader.network.json.PresenceJSON;

/**
 * Represents a user's status on Discord
 * 
 * @author Perry Berman
 */
public class Presence implements IPresence {

	/**
	 * The {@link Game} the user is currently playing, or null if user isn't playing
	 * a game
	 * 
	 * 
	 * @see IGame
	 * @see IUser
	 * @see IGuildMember
	 */
	public Game game;
	/**
	 * The status of the current user. can be {@literal online}, {@literal offline},
	 * {@literal idle}, or {@literal invisible}
	 * 
	 * 
	 */
	public String status;

	public Presence(PresenceJSON presence) {
		this.game = presence.game != null ? new Game(presence.game) : null;
		this.status = presence.status;
	}

	public Presence(String status, GameJSON game) {
		this.game = new Game(game);
		this.status = status;
	}

	protected Presence(String status, Game game) {
		this.game = game;
		this.status = status;
	}

	public Presence() {
		this.game = null;
		this.status = "offline";
	}

	public Presence(Presence data) {
		this.game = data.game != null ? new Game(data.game) : null;
		this.status = data.status;
	}

	public void update(PresenceJSON presence) {
		this.update(presence.status, presence.game != null ? new Game(presence.game) : null);
	}

	public void update(String status, Game game) {
		this.status = status != null ? status : this.status;
		this.game = game;
	}

	public void update(String status) {
		this.update(status, this.game);
	}

	public void update(Game game) {
		this.update(null, game);
	}

	@Override
	public Presence clone() {
		return new Presence(status, game);
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public IGame getGame() {
		return game;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof IPresence))
			return false;
		IPresence p = (IPresence) obj;
		return status.equals(p.getStatus()) && game.equals(p.getGame());
	}

	public int hashCode() {
		if (game != null)
			return (status + game.hashCode()).hashCode();
		return status.hashCode();
	}

}
