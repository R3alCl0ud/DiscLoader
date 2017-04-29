package io.discloader.discloader.client.render.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Perry Berman
 */
public class Resource {
	
	private String location;
	private String name;
	
	public Resource(String domain, String name) {
		location = domain;
		this.name = name;
	}
	
	public String getDomain() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFileName() {
		String[] l = getPath().split("/");
		return l[l.length - 1];
	}
	
	public String getPath() {
		return "/assets/" + location + "/" + name;
	}
	
	public File getFile() throws IOException {
		// try loading from the ClassLoader
		URL url = ClassLoader.getSystemClassLoader().getResource(getPath());
		// try from the class it's self because java just doesn't understand how file systems work
		if (url == null)
			url = Resource.class.getResource(getPath());
		// since we still can't find it, throw the exception
		if (url == null)
			throw new FileNotFoundException(getPath());
		File file = new File(url.getFile());
		return file;
	}
	
	public InputStream getResourceAsStream() {
		InputStream is = Resource.class.getResourceAsStream(getPath());
		return is == null ? ClassLoader.getSystemResourceAsStream(getPath()) : is;
	}
	
	public URL toURL() {
		return Resource.class.getResource(getPath());
	}
	
}
