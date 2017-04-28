package io.discloader.discloader.entity.embed;

import java.io.File;

import io.discloader.discloader.client.render.util.Resource;

/**
 * @author Perry Berman
 */
public class EmbedImage {

	public String url;

	public transient File file;

	public transient Resource resource;
	
	public EmbedImage(String url) {
		this.url = url;
	}

	public EmbedImage(Resource resource) {
		this(String.format("attachment://%s", resource.getFileName()));
	}
	
	public EmbedImage(File img) {
		this(String.format("attachment://%s", img.getName()));

	}

}
