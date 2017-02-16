package io.discloader.discloader.common.registry;

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

import io.discloader.discloader.common.discovery.Mod.EventHandler;
import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.start.Start;

/**
 * Deprecated <u>DO NOT USE</u>
 * @author Perry Berman
 *
 */
@Deprecated
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

		for (int i = 0; i < jars.length; i++) {
			File jar = jars[i];
			if (Start.window != null) {
				LoadingPanel.setStep(i + 1, jars.length, String.format("Checking file: %s", jar.getName()));
			}

			Matcher modMatch = modExt.matcher(jar.getName());
			if (!modMatch.find()) {
				System.out.printf("Found non-mod file in mods directory: %s\n", jar.getName());
				continue;
			}
			String file_ext = modMatch.groupCount() == 1 ? modMatch.group(1) : "";
			if (file_ext == null)
				continue;
			if ((file_ext.toLowerCase().equals("jar")) && !jar.equals("jar")) {
				System.out.println("\tJarfile/Zip Name: " + jar.getName());
				try {
					JarFile jf = new JarFile(jar);
					e = jf.entries();

					URL[] urls = { new URL("jar:file:" + jar.getPath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					ArrayList<JarEntry> entries = getEntries(e);
					for (int n = 0; n < entries.size(); n++) {
						JarEntry jarEntry = entries.get(n);
						if (Start.window != null) {
							LoadingPanel.setProgress(n + 1, entries.size(), jarEntry.getName());
						}

						String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
						className = className.replace('/', '.');

						potentMod = cl.loadClass(className);

						if (potentMod.isAnnotationPresent(Mod.class)) {
							System.out.printf("Found a mod: %s\n", potentMod.getName());
//							list.add(new ModContainer(potentMod));
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
		/*int i = 0, n = modContainer.modClass.getDeclaredMethods().length;
		for (Method method : modContainer.modClass.getDeclaredMethods()) {
			i++;
			if (Start.window != null) {
				LoadingPanel.setProgress(i, n, method.getName());
			}
			if (method.isAnnotationPresent(EventHandler.class)) {
				eventHandlers.add(method);
			}
		}*/
		return eventHandlers;
	}

	private static ArrayList<JarEntry> getEntries(Enumeration<JarEntry> jarDir) {
		ArrayList<JarEntry> entries = new ArrayList<JarEntry>();
		while (jarDir.hasMoreElements()) {
			JarEntry entry = jarDir.nextElement();
			if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
				continue;
			}
			entries.add(entry);
		}
		return entries;
	}

}
