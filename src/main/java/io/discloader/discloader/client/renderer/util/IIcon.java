package io.discloader.discloader.client.renderer.util;

import java.awt.Image;

/**
 * @author Perry Berman
 *
 */
public interface IIcon {
	
	
	int getIconHeight();
	
	int getIconWidth();
	
	String getIconName();
	
	Image getImage();
	
	void setIconHeight(int height);
	
	void setIconWidth(int width);
	
	void setIconName(String iconName);
	
	void setImage(Image image);
	
}
