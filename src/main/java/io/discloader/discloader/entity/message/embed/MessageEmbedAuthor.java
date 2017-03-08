package io.discloader.discloader.entity.message.embed;

/**
 * Represents an embed author
 * 
 * @author Perry Berman
 *
 */
public class MessageEmbedAuthor {

	private String url;

	private String name;

	private String iconURL;

	public MessageEmbedAuthor(String name, String url, String icon_url) {
		this.name = name;
		this.url = url;
		this.iconURL = icon_url;
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

}
