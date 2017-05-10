package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.core.entity.invite.InviteGuild;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.util.DLUtil.Endpoints;

/**
 * @author Perry Berman
 */
public class GuildIcon extends AbstractTexture implements IIcon {

	public final IGuild guild;
	private String hash;

	public GuildIcon(IGuild guild, String icon) {
		this.guild = guild;
		hash = icon;
		this.setIconHeight(128);
		this.setIconWidth(128);
		this.setIconName(hash != null ? hash : Long.toUnsignedString(guild.getID()));

	}

	public GuildIcon(InviteGuild inviteGuild) {
		this.guild = null;
		this.setIconHeight(128);
		this.setIconWidth(128);
		this.setIconName(inviteGuild.iconHash != null ? inviteGuild.iconHash : "" + inviteGuild.getID());
	}

	@Override
	public ImageIcon getImageIcon() {
		return this.createImageIcon(toString());
	}

	@Override
	public Image getImage() {
		return this.getImageIcon().getImage();
	}

	protected ImageIcon createImageIcon(String url) {
		URL imgURL = null;
		try {
			imgURL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			imgURL = ClassLoader.getSystemResource("assets/discloader/texture/gui/icons/missing-icon.png");
		}
		if (imgURL != null) {
			return new ImageIcon(imgURL, guild.getName());
		} else {
			System.err.println("Couldn't find file: " + url);
			return null;
		}
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
