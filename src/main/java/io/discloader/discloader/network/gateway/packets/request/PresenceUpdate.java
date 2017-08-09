package io.discloader.discloader.network.gateway.packets.request;

import io.discloader.discloader.core.entity.Game;

public class PresenceUpdate {
	public Game game;
	public boolean afk;
	public String status;
	public int since;

	public PresenceUpdate(Game game, String status, boolean afk, int since) {
		this.game = game;
		this.since = since;
		this.afk = afk;
		this.status = status;
	}
}
