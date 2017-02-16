package io.discloader.discloader.client.renderer.texture;

import io.discloader.discloader.client.renderer.util.IIcon;

/**
 * @author Perry Berman
 *
 */
public class CommandIcon extends AbstractTexture implements IIcon {
	private int iconHeight;
	private int iconWidth;
	private String iconName;
	
	
	
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
		return iconWidth;
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.renderer.util.IIcon#getIconName()
	 */
	@Override
	public String getIconName() {
		return iconName;
	}

}
