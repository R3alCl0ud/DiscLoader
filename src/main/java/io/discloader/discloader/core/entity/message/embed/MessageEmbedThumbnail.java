package io.discloader.discloader.core.entity.message.embed;

import java.io.File;

/**
 * @author Perry Berman
 *
 */
public class MessageEmbedThumbnail {

	public String url;

	public File file;
	
	public MessageEmbedThumbnail(String url) {
		this.url = url;
		
		this.file = null;
	}
	
	public MessageEmbedThumbnail(File file) {
		this.url = String.format("attachment://%s", file.getName());
		
		this.file = file;
	}

	public String toString() {
		return String.format("{\"url\":\"%s\"}", this.url);
	}
}
