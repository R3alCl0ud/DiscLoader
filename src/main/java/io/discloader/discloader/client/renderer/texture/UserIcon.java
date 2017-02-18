package io.discloader.discloader.client.renderer.texture;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.common.structures.User;

/**
 * @author Perry Berman
 *
 */
public class UserIcon extends AbstractTexture {
	
	public final User user;
	
	public UserIcon(User user) {
		super();
		this.user = user;
		this.setIconHeight(128);
		this.setIconWidth(128);
		this.setIconName(user.avatar);
		this.setImageIcon(this.createImageIcon(user.avatarURL));
		this.setImage(this.getImageIcon().getImage());
	}

	protected ImageIcon createImageIcon(String url) {
		URL imgURL = null;
		try {
			imgURL = new URL(url);
		} catch (MalformedURLException e) {
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
