package io.discloader.discloader.entity.sendable;

import io.discloader.discloader.core.entity.Game;

public class SendablePresenceUpdate {
	public Game game;
	public boolean afk;
	public String status;
	public int since;

	public SendablePresenceUpdate(Game game, String status, boolean afk, int since) {
		this.game = game;
		this.since = since;
		this.afk = afk;
		this.status = status;
	}
}
