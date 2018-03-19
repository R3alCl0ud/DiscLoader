package io.discloader.discloader.core.entity.guild;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.util.DLUtil.Endpoints;

/**
 * @author Perry Berman
 */
public class GuildIcon implements IIcon {

	public final IGuild guild;
	private String hash;

	public GuildIcon(IGuild guild, String icon) {
		this.guild = guild;
		hash = icon;
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(Endpoints.guildIcon(guild.getID(), hash));
	}

	@Override
	public String getHash() {
		return hash;
	}

	@Override
	public String toString() {
		return Endpoints.guildIcon(guild.getID(), hash);
	}
}
