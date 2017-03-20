package io.discloader.discloader.entity.sendable;

public class SendablePresenceUpdate {
	public String game;
	public boolean afk;
	public String status;
	public int since;

	public SendablePresenceUpdate(String game, String status, boolean afk, int since) {
		this.game = game;
		this.since = since;
		this.afk = afk;
		this.status = status;
	}
}
