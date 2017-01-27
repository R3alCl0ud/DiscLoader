package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.GameJSON;
import io.disc.DiscLoader.objects.gateway.PresenceJSON;

public class Presence {
	public Game game;
	public String status;
	public Presence(PresenceJSON presence) {
		this.game = presence.game != null ? new Game(presence.game) : null;
		this.status = presence.status;
	}
	
	public Presence(String status, GameJSON game) {
		this.game = new Game(game);
		this.status = status;
	}

	public Presence() {
		this.game = null;
		this.status = "offline";
	}
	
	public void update(PresenceJSON presence) {
		this.game = presence.game != null ? new Game(presence.game) : null;
		this.status = presence.status != null ? presence.status : this.status;
	}
	
}
