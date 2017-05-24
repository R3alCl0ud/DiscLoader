package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedAuthor;
import io.discloader.discloader.network.json.EmbedAuthorJSON;

/**
 * Represents an embed author
 * 
 * @author Perry Berman
 */
public class MessageEmbedAuthor implements IEmbedAuthor {

	private String url, name, iconURL, proxyURL;

	public MessageEmbedAuthor(EmbedAuthorJSON data) {
		this(data.name, data.url, data.icon_url, data.proxy_icon_url);
	}

	public MessageEmbedAuthor(String n, String u, String icon_url, String proxy_url) {
		name = n;
		url = u;
		iconURL = icon_url;
		proxyURL = proxy_url;
	}

	public MessageEmbedAuthor(String name, String url, String icon_url) {
		this(name, url, icon_url, null);
	}

	public MessageEmbedAuthor(String name, String url) {
		this(name, url, null);
	}

	public MessageEmbedAuthor(String name) {
		this(name, null, null);
	}

	/**
	 * @return The name of the author
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The URL that links to the author's icon
	 */
	public String getIconURL() {
		return iconURL;
	}

	/**
	 * @return The link to the author's website
	 */
	public String getURL() {
		return url;
	}

	@Override
	public String getProxyIconURL() {
		return proxyURL;
	}

}
