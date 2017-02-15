package io.discloader.discloader.objects.loader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import io.discloader.discloader.objects.annotations.EventHandler;
import io.discloader.discloader.objects.annotations.Mod;
import io.discloader.discloader.objects.mods.ModContainer;

/**
 * @author Perry Berman
 *
 */
public class ServiceLoader {

	private static Class<?> potentMod;

	protected static Enumeration<JarEntry> e;

	public static ArrayList<ModContainer> loadMods() {
		ArrayList<ModContainer> list = new ArrayList<ModContainer>();
		// Determine if the mods folder exists already
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
			/*
			 * Emergency Code Latch IN CASE OF EMERGENCY UN-COMMENT CODE
			 * -------------------------------------------------
			 * System.out.println(jars[i].getName() + " " + file_ext +
			 * " IS JAR? " + (file_ext.toLowerCase().equals("jar") &&
			 * !jars[i].equals("jar") ? "TRUE" : "FALSE"));
			 */
			if ((file_ext.toLowerCase().equals("jar")) && !jars[i].equals("jar")) {
				System.out.println("\tJarfile/Zip Name: " + jars[i].getName());
				try {
					JarFile jf = new JarFile(jars[i]);
					e = jf.entries();

					URL[] urls = { new URL("jar:file:" + jars[i].getPath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					while (e.hasMoreElements()) {
						JarEntry je = e.nextElement();
						if (je.isDirectory() || !je.getName().endsWith(".class")) {
							continue;
						}

						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');

						potentMod = cl.loadClass(className);
						
						if (potentMod.isAnnotationPresent(Mod.class)) {
							System.out.printf("Found a mod: %s\n", potentMod.getName());
							list.add(new ModContainer(potentMod));
						}
					}

					jf.close();
				} catch (Exception e) {
					System.out.println("Unable to add JarFile " + jars[i].getName());
				}
			}
		}

		return list;
	}

	public static ArrayList<Method> loadEventHandlers(ModContainer modContainer) {
		ArrayList<Method> eventHandlers = new ArrayList<Method>();
		for (Method method : modContainer.modClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventHandler.class)) {
				eventHandlers.add(method);
			}
		}
		return eventHandlers;
	}

}
