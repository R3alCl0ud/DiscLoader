package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedImage;
import io.discloader.discloader.network.json.EmbedImageJSON;

/**
 * @author Perry Berman
 */
public class MessageEmbedImage implements IEmbedImage {

	private int width, height;
	private String url, proxyURL;

	public MessageEmbedImage(EmbedImageJSON data) {
		width = data.width;
		height = data.height;
		url = data.url;
		proxyURL = data.proxy_url;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public String getProxyURL() {
		return proxyURL;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public int getWidth() {
		return width;
	}

}
