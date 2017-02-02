package io.disc.DiscLoader.objects.loader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModHandler {
	private ClassLoader modhandler;
	private Class<?> instance;
	private Method methods[];
	
	// Set annotation class name
	private String ann_class = "io.disc.DiscLoader.objects.loader.Mod.class";
	
	protected Enumeration<JarEntry> e;
	
	public void beginLoader(String customAnnotationPath) {
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
		
		// This will print out the working directory.
		System.out.println(System.getProperty("user.dir"));
		
		// Hopefully it will be jar files
		File jars[] = dir.listFiles();
		
		for (int i = 0; i < jars.length; i++) {
			String file_ext = jars[i].getName().split("\\.")[jars[i].getName().split("\\.").length - 1];
			/* Emergency Code Latch
			 * IN CASE OF EMERGENCY UN-COMMENT CODE
			 * -------------------------------------------------
			 * System.out.println(jars[i].getName() + " " + file_ext + " IS JAR? " + (file_ext.toLowerCase().equals("jar") && !jars[i].equals("jar") ? "TRUE" : "FALSE"));
			 */
			if ((file_ext.toLowerCase().equals("jar")) && !jars[i].equals("jar")) {
				System.out.println("\tJarfile/Zip Name: " + jars[i].getName());
				try {
					JarFile jf = new JarFile(jars[i]);
					this.e = jf.entries();
					
					URL[] urls = { new URL("jar:file:" + jars[i].getPath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);
					
					while (this.e.hasMoreElements()) {
						JarEntry je = e.nextElement();
						if (je.isDirectory() || !je.getName().endsWith(".class")) {
							continue;
						}
						
						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');
						
						try {
							this.instance = cl.loadClass(className);
							System.out.println(this.instance.getName());
							for (int j = 0, n = this.instance.getMethods().length; j < n; j++) {
								System.out.println("\tMethod Name: " + this.instance.getMethods()[i].getName());
							}
						} catch (Exception ce) {
							System.out.println("Class unable to load: " + className);
						}
					}
					
					jf.close();
				} catch (Exception e) {
					System.out.println("Unable to add JarFile" + jars[i].getName());
				}
			}	
		}
	}
	
	public void beginLoader() {
		this.beginLoader(null);
	}
}
