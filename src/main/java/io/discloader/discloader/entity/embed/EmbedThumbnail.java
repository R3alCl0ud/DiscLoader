package io.discloader.discloader.entity.embed;

import java.io.File;

import io.discloader.discloader.client.render.util.Resource;

/**
 * @author Perry Berman
 */
public class EmbedThumbnail {

	public String url;

	public transient File file = null;

	public transient Resource resource = null;

	public EmbedThumbnail(Resource resource) {
		url = String.format("attachment://%s", resource.getFileName());

		this.resource = resource;
	}

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
