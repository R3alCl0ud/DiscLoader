package io.disc.DiscLoader.objects.loader;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Optional;

public class ModHandler {
	private ClassLoader modhandler;
	private Class<?> instance;
	private Method methods[];
	
	// Set annotation class name
	private String ann_class = "io.disc.DiscLoader.objects.loader.Mod.class";
	
	public void beginLoader(String customAnnotationPath) {
		Method entryPoint;
		if (customAnnotationPath != null) this.ann_class = customAnnotationPath;
		
		// Determine if mod loading folder exists already
		File dir = (new File("mods"));
		if (!dir.exists() || !dir.isDirectory()) {
			try {
				boolean success = (new File("mods")).mkdirs();
				if (!success) {
					System.out.println("Error occurred while making directory: mods");
					System.exit(1);
				}
			} catch (Exception e) {
				System.out.println("Error occurred while making directory: mods");
				System.exit(1);
			}
		}
		
		try {
			dir = (new File("mods"));
		} catch (Exception e) {
			System.out.println("Error occurred while making directory: mods");
			System.exit(1);
		}
		
		if (!dir.isDirectory()) {
			System.out.println("Error occurred while reading directory: mods");
			System.out.println("\tReported as: NOT A DIR");
			System.exit(1);
		}
		
		System.out.println(System.getProperty("user.dir"));
	}
}
