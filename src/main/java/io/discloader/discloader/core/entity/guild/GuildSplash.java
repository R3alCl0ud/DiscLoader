package io.discloader.discloader.core.entity.guild;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.core.entity.invite.InviteGuild;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.util.DLUtil.Endpoints;

/**
 * 
 * @author Perry Berman
 */
public class GuildSplash implements IIcon {

	private Guild guild = null;
	private InviteGuild partGuild = null;

	public GuildSplash(Guild guild) {
		this.guild = guild;
	}

	public GuildSplash(InviteGuild guild) {
		this.partGuild = guild;
	}

	public String toString() {
		if ((guild != null && guild.getSplashHash() == null) || (partGuild != null && partGuild.splashHash == null)) {
			return null;
		} else if (guild != null) {
			return Endpoints.guildSplash(guild.getID(), guild.getSplashHash());
		} else {
			return Endpoints.guildSplash(partGuild.getID(), partGuild.splashHash);
		}
	}

	@Override
	public String getHash() {
		return guild == null ? partGuild != null ? partGuild.splashHash : null : guild.getSplashHash();
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(toString());
	}

}
