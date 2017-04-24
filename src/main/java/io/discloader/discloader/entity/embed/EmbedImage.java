package io.discloader.discloader.entity.embed;

import java.io.File;

/**
 * @author Perry Berman
 */
public class EmbedImage {

	public String url;

	public transient File file;

	public EmbedImage(String url) {
		this.url = url;
	}

	public EmbedImage(File img) {
		url = String.format("attachment://%s", img.getName());

	}

}
