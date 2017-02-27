package io.discloader.discloader.client.render;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.jar.JarEntry;

import io.discloader.discloader.common.language.LanguageParser;

public class ResourceHandler {

	public final HashMap<String, File> resources;

	public ResourceHandler() {
		this.resources = new HashMap<String, File>();
		// this.resourceDir = new File("assets");
	}

	public void addResource(File resource) {

	}

	public void addResource(JarEntry resource) {
		try {
			File f = File.createTempFile(resource.getName(), null);
			FileOutputStream resourceOS = new FileOutputStream(f);
			byte[] byteArray = new byte[1024];
			int i;
			InputStream classIS = getClass().getClassLoader().getResourceAsStream(resource.getName());
			// While the input stream has bytes
			while ((i = classIS.read(byteArray)) > 0) {
				// Write the bytes to the output stream
				resourceOS.write(byteArray, 0, i);
			}
			// Close streams to prevent errors
			classIS.close();
			resourceOS.close();
			this.resources.put(resource.getName(), f);
			if (this.isLanguage(f)) {
				LanguageParser.parseLang(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isLanguage(File f) {
		if (!f.getName().endsWith(".lang")) {
			return false;
		}

		return true;
	}

}