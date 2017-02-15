package io.discloader.discloader.objects.loader;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static Pattern modExt = Pattern.compile(".(jar|zip)");

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

		for (File jar : jars) {
			Matcher modMatch = modExt.matcher(jar.getName());
			System.out.println("hmm");
			String file_ext = null;//modMatch.groupCount() == 1 ? modMatch.group(0) : "";
			System.out.println(jar.getName());
			if (file_ext == null) continue;
			System.out.println(file_ext);
			if ((file_ext.toLowerCase().equals("jar")) && !jar.equals("jar")) {
				System.out.println("\tJarfile/Zip Name: " + jar.getName());
				try {
					JarFile jf = new JarFile(jar);
					e = jf.entries();

					URL[] urls = { new URL("jar:file:" + jar.getPath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					while (e.hasMoreElements()) {
						JarEntry jarEntry = e.nextElement();
						if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
							continue;
						}

						String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
						className = className.replace('/', '.');

						potentMod = cl.loadClass(className);

						if (potentMod.isAnnotationPresent(Mod.class)) {
							System.out.printf("Found a mod: %s\n", potentMod.getName());
							list.add(new ModContainer(potentMod));
						}
					}

					jf.close();
				} catch (Exception e) {
					System.out.println("Unable to add JarFile " + jar.getName());
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
