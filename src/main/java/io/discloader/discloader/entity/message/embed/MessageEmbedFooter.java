package io.discloader.discloader.entity.message.embed;

/**
 * Represents an embed's footer
 * 
 * @author Perry Berman
 *
 */
public class MessageEmbedFooter {

	private String text;

	private String iconURL;

	public MessageEmbedFooter(String text, String icon_url) {
		this.text = text;
		this.iconURL = icon_url;
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
		return this.iconURL;
	}

}
