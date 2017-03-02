package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.entity.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildIcon extends AbstractTexture {

	public final Guild guild;

	public GuildIcon(Guild guild) {
		this.guild = guild;
		this.setIconHeight(128);
		this.setIconWidth(128);
		this.setIconName(guild.icon != null ? guild.icon : guild.id);

	}

	@Override
	public ImageIcon getImageIcon() {
		return this.createImageIcon(this.guild.iconURL);
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
			return new ImageIcon(imgURL, this.guild.name);
		} else {
			System.err.println("Couldn't find file: " + url);
			return null;
		}
	}
}
