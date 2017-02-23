package io.discloader.discloader.client.render.texture.icon;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.util.Constants;

/**
 * @author Perry Berman
 *
 */
public class CommandIcon extends AbstractTexture implements IIcon {

	public CommandIcon(Command command) {
		this.setIconName(command.getTextureName());
	}

	public String getNamespace() {
		return this.getIconName().substring(0, this.getIconName().indexOf(':'));
	}

	public String getNamespaceTexture() {
		return this.getIconName().substring(this.getIconName().indexOf(':') + 1);
	}

	public File getFile() {
		String path = String.format("assets/%s/texture/icon/commands/%s.png", this.getNamespace(),
				this.getNamespaceTexture().replace('.', '/'));
		if (TextureRegistry.resourceHandler.resources.containsKey(path)) {
			return TextureRegistry.resourceHandler.resources.get(path);
		}
		File file = new File(ClassLoader.getSystemResource(path).getFile());
		if (file.exists() && file.isFile()) {
			return file;
		}
		return Constants.MissingTexture;
	}

	protected ImageIcon createImageIcon(String path) {
		URL imgURL = ClassLoader.getSystemResource(String.format("assets/%s/texture/icon/commands/%s.png", this.getNamespace(),
				this.getNamespaceTexture().replace('.', '/')));
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
