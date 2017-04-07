package io.discloader.discloader.client.render.texture.icon;

import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.render.ResourceManager;
import io.discloader.discloader.client.render.texture.AbstractTexture;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 *
 */
public class CommandIcon extends AbstractTexture implements IIcon {
	
	private Command command;
	
	public CommandIcon(Command command) {
		this.setIconName(command.getTextureName());
		this.command = command;
	}
	
	@Override
	public ImageIcon getImageIcon() {
		return this.createImageIcon(this.getIconName());
	}
	
	@Override
	public Image getImage() {
		return this.getImageIcon().getImage();
	}
	
	public String getNamespace() {
		return this.getIconName().substring(0, this.getIconName().indexOf(':'));
	}
	
	public String getNamespaceTexture() {
		return this.getIconName().substring(this.getIconName().indexOf(':') + 1);
	}
	
	public File getFile() {
		Resource ic = null;
		ic = command.getResourceLocation();
		if (ic == null)
			ic = ResourceManager.instance.getResource("texture/commands/" + getNamespaceTexture() + ".png");
		
		try {
			if (ic != null)
				return ic.getFile();
			return DLUtil.MissingTexture;
		} catch (Exception e) {
			return DLUtil.MissingTexture;
		}
	}
	
	protected ImageIcon createImageIcon(String path) {
		URL imgURL = null;
		try {
			imgURL = getFile().toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			imgURL = ClassLoader.getSystemResource(String.format("assets/%s/texture/commands/%s.png", this.getNamespace(), this.getNamespaceTexture().replace('.', '/')));
		}
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			try {
				imgURL = DLUtil.MissingTexture.toURI().toURL();
				return new ImageIcon(imgURL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
