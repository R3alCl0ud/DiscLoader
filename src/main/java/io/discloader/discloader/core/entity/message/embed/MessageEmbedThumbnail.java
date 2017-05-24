package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedThumbnail;
import io.discloader.discloader.network.json.EmbedThumbnailJSON;

/**
 * @author Perry Berman
 */
public class MessageEmbedThumbnail implements IEmbedThumbnail {

	public String url, proxyURL;
	public int width, height;

	public MessageEmbedThumbnail(EmbedThumbnailJSON data) {
		url = data.url;
		proxyURL = data.proxy_url;
		width = data.width;
		height = data.height;
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
