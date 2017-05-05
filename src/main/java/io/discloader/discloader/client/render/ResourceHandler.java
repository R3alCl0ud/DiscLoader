package io.discloader.discloader.client.render;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;

import io.discloader.discloader.common.language.LanguageParser;

public class ResourceHandler {

	public static ResourceHandler instance = new ResourceHandler();

	public final HashMap<String, File> resources = new HashMap<>();

	public void addResource(File resource) {
		this.resources.put(resource.getName(), resource);
	}

	public void addResource(InputStream resourceIS, ZipEntry resource) {
		File f;
		try {
			f = File.createTempFile(resource.getName(), null);
			FileOutputStream resourceOS = new FileOutputStream(f);
			byte[] byteArray = new byte[1024];
			int i;
			while ((i = resourceIS.read(byteArray)) > 0) {
				// Write the bytes to the output stream
				resourceOS.write(byteArray, 0, i);
			}
			resourceIS.close();
			resourceOS.close();
			this.resources.put(resource.getName(), f);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addResource(JarEntry resource) {
		try {
			File f = File.createTempFile(resource.getName(), null);
			FileOutputStream resourceOS = new FileOutputStream(f);
			byte[] byteArray = new byte[1024];
			int i;
			InputStream classIS = getClass().getClassLoader().getResourceAsStream(resource.getName());
			// While the input stream has bytes and the input stream isn't null
			if (classIS != null) {
				while ((i = classIS.read(byteArray)) > 0) {
					// Write the bytes to the output stream
					resourceOS.write(byteArray, 0, i);
				}
				// Close streams to prevent errors
				classIS.close();
			}
			resourceOS.close();
			this.resources.put(resource.getName(), f);
			if (this.isLanguage(f)) {
				LanguageParser.parseLang(new FileInputStream(f));
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