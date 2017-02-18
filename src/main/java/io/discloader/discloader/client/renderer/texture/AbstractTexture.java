package io.discloader.discloader.client.renderer.texture;

import java.awt.Image;

import javax.swing.ImageIcon;

import io.discloader.discloader.client.renderer.util.IIcon;

/**
 * Just a prototype for textures
 * 
 * @author Perry Berman
 *
 */
public abstract class AbstractTexture implements IIcon {
	private int iconHeight;
	private int iconWidth;
	private String iconName;

	private Image image;
	private ImageIcon imageIcon;
	
	public AbstractTexture() {
		
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
		return this.iconHeight;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
		return this.iconWidth;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#getIconName()
	 */
	@Override
	public String getIconName() {
		return this.iconName;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#getImage()
	 */
	@Override
	public Image getImage() {
		return this.image;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#setIconHeight(int)
	 */
	@Override
	public void setIconHeight(int height) {
		this.iconHeight = height;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#setIconWidth(int)
	 */
	@Override
	public void setIconWidth(int width) {
		this.iconWidth = width;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#setIconName(java.lang.String)
	 */
	@Override
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#setImage(java.awt.Image)
	 */
	@Override
	public void setImage(Image image) {
		this.image = image;
	}


	public ImageIcon getImageIcon() {
		return this.imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}
}
