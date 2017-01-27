package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.GameJSON;
import io.disc.DiscLoader.objects.gateway.PresenceJSON;

public class Presence {
	public Game game;
	public String status;
	public final User user;
	public final Guild guild;
	public final DiscLoader loader;
	public Presence(User user, Guild guild, PresenceJSON presence) {
		this.game = new Game(presence.game);
		this.status = presence.status;
		this.user = user;
		this.guild = guild;
		this.loader = this.guild.loader;
	}
	
	public Presence(User user, Guild guild, String status, GameJSON game) {
		this.game = new Game(game);
		this.status = status;
		this.user = user;
		this.guild = guild;
		this.loader = this.guild.loader;
	}

	public Presence(Guild guild, User user, PresenceJSON guildPresence) {
		this.game = new Game(guildPresence.game);
		this.status = guildPresence.status;
		this.guild = guild;
		this.loader = this.guild.loader;
		this.user = user;
	}
}
