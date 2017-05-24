package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedVideo;
import io.discloader.discloader.network.json.EmbedVideoJSON;

/**
 * @author Perry Berman
 */
public class MessageEmbedVideo implements IEmbedVideo {

	private int width, height;
	private String url;

	public MessageEmbedVideo(EmbedVideoJSON data) {
		url = data.url;
		height = data.height;
		width = data.width;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

}
