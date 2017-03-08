package io.discloader.discloader.entity.embed;

import java.io.File;

/**
 * @author Perry Berman
 *
 */
public class EmbedThumbnail {

	public String url;

	public File file;
	
	public EmbedThumbnail(File file) {
		this.url = String.format("attachment://%s", file.getName());
		
		this.file = file;
	}
	
	public EmbedThumbnail(String url) {
		this.url = url;
		
		this.file = null;
	}

	public String toString() {
		return String.format("{\"url\":\"%s\"}", this.url);
	}
}
