package io.discloader.discloader.core.entity;

import io.discloader.discloader.entity.IGame;
import io.discloader.discloader.network.json.GameJSON;

public class Game implements IGame {

	/**
	 * The name of the game being played or streamed
	 */
	private String name;

	/**
	 * The type of activity
	 */
	private transient int type;

	/**
	 * The link to the user's Twitch Stream. (Currently it is only possible to
	 * stream using Twitch on discord)<br>
	 * is null if {@link #isStream()} is {@code false}
	 */
	public String url;

	public Game(Game game) {
		this.name = game.name;
		this.type = game.type;
		this.url = game.url;
	}

	public Game(GameJSON game) {
		this.name = game.name;
		this.type = game.type;
		this.url = game.url;
	}

	public Game(String name) {
		this.name = name;
		this.type = 0;
		this.url = null;
	}

	/**
	 * Checks if {@code this} is a live stream
	 * 
	 * @return true {@code if }{@link #type} {@code == 1}
	 */
	public boolean isStream() {
		return type != 0 && type == 1;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IGame)) return false;
		IGame game = (IGame) obj;

		return name.equals(game.getName()) && isStream() == game.isStream() && url.equals(game.getURL());
	}

	@Override
	public String getURL() {
		return url;
	}

	public int hashCode() {
		return (name + type + url).hashCode();
	}
}
