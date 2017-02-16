package io.discloader.discloader.client.registry;

import java.util.HashMap;

import io.discloader.discloader.client.renderer.texture.AbstractTexture;

/**
 * @author Perry Berman
 *
 */
public class TextureRegistry {

	private HashMap<Integer, AbstractTexture> icons;
	
	public TextureRegistry() {
		this.icons = new HashMap<Integer, AbstractTexture>();
	}

	/**
	 * @return the icons
	 */
	public HashMap<Integer, AbstractTexture> getIcons() {
		return icons;
	}

	/**
	 * @param icons the icons to set
	 */
	public void setIcons(HashMap<Integer, AbstractTexture> icons) {
		this.icons = icons;
	}

}
