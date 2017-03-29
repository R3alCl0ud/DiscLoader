package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.entity.invite.InviteGuild;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Endpoints;

public class GuildSplash extends AbstractTexture {

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
		if ((guild != null && guild.splashHash == null) || (partGuild != null && partGuild.splashHash == null)) {
			return null;
		} else if (guild != null) {
			return Endpoints.guildSlash(guild.getID(), guild.splashHash);
		} else {
			return Endpoints.guildSlash(partGuild.id, partGuild.splashHash);
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

}
