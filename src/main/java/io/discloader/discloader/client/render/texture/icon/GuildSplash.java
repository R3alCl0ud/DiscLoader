package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.invite.InviteGuild;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;

public class GuildSplash extends AbstractTexture implements IIcon {

	private Guild guild = null;
	private InviteGuild partGuild = null;

	public GuildSplash(Guild guild) {
		this.guild = guild;
	}

	public GuildSplash(InviteGuild guild) {
		this.partGuild = guild;
	}

	/**
	 * @author Perry Berman
	 * @return The link to the splash's image
	 */
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
	public ImageIcon getImageIcon() {
		return this.createIcon(toString());
	}

	@Override
	public Image getImage() {
		return this.getImageIcon().getImage();
	}

	protected ImageIcon createIcon(String url) {
		URL imgURL = null;
		if (url != null) {
			try {
				imgURL = new URL(url);
			} catch (MalformedURLException e) {
				try {
					imgURL = DLUtil.MissingTexture.toURI().toURL();
				} catch (MalformedURLException e1) {
				}
			}
		}
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			return null;
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
