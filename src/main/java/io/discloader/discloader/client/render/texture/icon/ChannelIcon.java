/**
 * 
 */
package io.discloader.discloader.client.render.texture.icon;

import java.io.File;
import java.nio.channels.Channel;

import io.discloader.discloader.client.render.texture.AbstractTexture;

/**
 * @author Perry Berman
 *
 */
public class ChannelIcon extends AbstractTexture {

	public ChannelIcon(Channel channel) {
		
	}

	/* (non-Javadoc)
	 * @see io.discloader.discloader.client.render.util.IIcon#getFile()
	 */
	@Override
	public File getFile() {
		return null;
	}

}
