package io.discloader.discloader.client.render.texture.icon;

import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.client.render.util.IIcon;

/**
 * @author Perry Berman
 *
 */
public class CommandIcon extends AbstractTexture implements IIcon {
	
	public CommandIcon(Command command) {
		this.setIconName(command.getTextureName());
		
	}
	
	protected ImageIcon createImageIcon(String path) {
		URL imgURL = ClassLoader.getSystemResource(String.format("assets/%s.png", path.replace('.', '/')));
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
