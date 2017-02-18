/**
 * 
 */
package io.discloader.discloader.client.renderer.texture;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.common.structures.Guild;

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
		this.setImageIcon(this.createImageIcon(guild.iconURL));
		this.setImage(this.getImageIcon().getImage());
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
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + url);
			return null;
		}
	}
}
