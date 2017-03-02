package io.discloader.discloader.client.render.util;

import java.awt.Image;
import java.io.File;

/**
 * @author Perry Berman
 *
 */
public interface IIcon {
	
	
	File getFile();
	
	int getIconHeight();
	
	String getIconName();
	
	int getIconWidth();
	
	Image getImage();
	
	void setIconHeight(int height);
	
	void setIconName(String iconName);
	
	void setIconWidth(int width);
	
	void setImage(Image image);
}
