package io.discloader.discloader.entity.invite;

import io.discloader.discloader.client.render.texture.icon.GuildIcon;
import io.discloader.discloader.client.render.texture.icon.GuildSplash;
import io.discloader.discloader.network.json.GuildJSON;

public class InviteGuild {
	public final String id;
	public final String name;
	public final String splashHash;
	public final String iconHash;
	public final GuildIcon icon;
	public final GuildSplash splash;

	public InviteGuild(GuildJSON data) {
		this.id = data.id;
		this.name = data.name;
		this.splashHash = data.splash;
		this.iconHash = data.icon;
		this.icon = new GuildIcon(this);
		this.splash = new GuildSplash(this);
	}
}
