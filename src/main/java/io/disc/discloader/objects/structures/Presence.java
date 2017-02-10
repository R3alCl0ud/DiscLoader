package io.disc.discloader.objects.structures;

import io.disc.discloader.objects.gateway.GameJSON;
import io.disc.discloader.objects.gateway.PresenceJSON;

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

	public Presence(Presence data) {
		this.game = data.game != null ? new Game(data.game) : null;
		this.status = data.status;
	}

	public void update(PresenceJSON presence) {
		this.game = presence.game != null ? new Game(presence.game) : null;
		this.status = presence.status != null ? presence.status : this.status;
	}

}
