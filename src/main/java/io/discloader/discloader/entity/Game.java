package io.discloader.discloader.entity;

import io.discloader.discloader.network.json.GameJSON;

public class Game {

	/**
	 * The name of the game being played or streamed
	 */
	public String name;

	/**
	 * The type of activity
	 */
	private transient int type;

	/**
	 * The link to the user's Twitch Stream. (Currently it is only possible to
	 * stream using Twitch on discord)<br>
	 * is null if {@link #isStreaming()} is {@code false}
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
	 * true {@code if }{@link #type} {@code == 1}
	 */
	public boolean isStream() {
		return this.type != 0 && this.type == 1 ? true : false;
	}
}
