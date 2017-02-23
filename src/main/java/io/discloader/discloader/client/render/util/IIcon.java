package io.discloader.discloader.client.render.util;

import java.awt.Image;
import java.io.File;

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
	
	File getFile();
}
