package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.entity.User;

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
		this.setIconName(user.avatar != null ? user.avatar : user.id);
	}
	
	@Override
	public ImageIcon getImageIcon() {
		return this.createImageIcon(this.user.avatarURL);
	}

	@Override
	public Image getImage() {
		return this.getImageIcon().getImage();
	}

	public Image getImage(int width, int height) {
		return this.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	public ImageIcon getImageIcon(int width, int height) {
		return new ImageIcon(this.getImage(width, height));
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
