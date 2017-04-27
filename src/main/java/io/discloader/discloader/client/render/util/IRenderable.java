package io.discloader.discloader.client.render.util;

import java.awt.Image;
import java.io.File;

/**
 * @author Perry Berman
 */
public interface IRenderable {

	File getFile();

	int getIconHeight();

	String getIconName();

	String toHash();

	int getIconWidth();

	Image getImage();

	void setIconHeight(int height);

	void setIconName(String iconName);

	void setIconWidth(int width);

	void setImage(Image image);
}
