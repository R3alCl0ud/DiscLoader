package io.discloader.discloader.entity.sendable.embed;

/**
 * @author Perry Berman
 *
 */
public class EmbedAuthor {

	/**
	 * The author's website
	 */
	public String url;
	
	/**
	 * Name of the author
	 */
	public String name;
	
	/**
	 * The link to the author's icon
	 */
	public String icon_url;
	
	public EmbedAuthor(String name, String url, String icon_url) {
		this.name = name;
		this.url = url;
		this.icon_url = icon_url;
	}
	
	public EmbedAuthor(String name, String url) {
		this(name, url, null);
	}
	
	public EmbedAuthor(String name) {
		this(name, null, null);
	}
}
