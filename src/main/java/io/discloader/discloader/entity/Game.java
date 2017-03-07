package io.discloader.discloader.entity;

import org.json.JSONObject;

import io.discloader.discloader.network.json.GameJSON;

public class Game {

	/**
	 * The name of the game being played or streamed
	 */
	public String name;

	/**
	 * The type of activity
	 */
	public int type;

	/**
	 * The link to the user's Twitch Stream. (Currently it is only possible to
	 * stream using Twitch on discord)<br>
	 * is null if {@link #streaming} is {@code false}
	 */
	public String url;

	/**
	 * true {@code if }{@link #type} {@code == 1}
	 */
	public boolean streaming;

	public Game(Game game) {
		this.name = game.name;
		this.type = game.type;
		this.url = game.url;
		this.streaming = this.type != 0 && this.type == 1 ? true : false;
	}

	public Game(GameJSON game) {
		this.name = game.name;
		this.type = game.type;
		this.url = game.url;
		this.streaming = this.type != 0 && this.type == 1 ? true : false;
	}

	public Game(String name) {
		this.name = name;
		this.type = 0;
		this.url = null;
		this.streaming = this.type != 0 && this.type == 1 ? true : false;
	}

	public String toJsonString() {
		return new JSONObject().put("name", this.name).put("type", this.type).put("url", this.url).toString();
	}
}
