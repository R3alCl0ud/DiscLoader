package io.discloader.discloader.common.structures.embed;

/**
 * @author Perry Berman
 *
 */
public class EmbedAuthor {

	public String url;
	
	public String name;
	
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
