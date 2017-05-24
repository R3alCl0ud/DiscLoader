package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedFooter;
import io.discloader.discloader.network.json.EmbedFooterJSON;

/**
 * Represents an embed's footer
 * 
 * @author Perry Berman
 */
public class MessageEmbedFooter implements IEmbedFooter {

	private String text, iconURL, proxyURL;

	public MessageEmbedFooter(EmbedFooterJSON data) {
		text = data.text;
		iconURL = data.icon_url;
		proxyURL = data.proxy_icon_url;
	}

	/**
	 * @return The footers content
	 */
	public String getContent() {
		return text;
	}

	/**
	 * @return The footer's iconURL
	 */
	public String getIconURL() {
		return iconURL;
	}

	@Override
	public String getProxyURL() {
		return proxyURL;
	}

}
