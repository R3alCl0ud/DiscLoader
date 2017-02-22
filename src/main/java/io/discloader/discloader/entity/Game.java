package io.discloader.discloader.entity;

import org.json.JSONObject;

import io.discloader.discloader.network.gateway.json.GameJSON;

public class Game {
	
	/**
	 * The name of the game being played or streamed
	 */
	public String name;
	
	/**
	 * 
	 */
	public int type;
	public String url;
	public boolean streaming;

	/**
	 * @param game
	 */
	public Game(GameJSON game) {
		this.name = game.name;
		this.type = game.type;
		this.url = game.url;
		this.streaming = this.type != 0 && this.type == 1 ? true : false;
	}

	/**
	 * @param game
	 */
	public Game(Game game) {
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
