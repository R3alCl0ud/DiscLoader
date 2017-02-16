package io.discloader.discloader.common.discovery;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.discloader.discloader.client.logging.ProgressLogger;

/**
 * 
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class ModDiscoverer {

	private static Pattern modExt = Pattern.compile(".(jar|zip)");

	public static final File modsDir = new File("./mods");

	public static void checkModDir() {
		ProgressLogger.progress(1, 3, "Looking for mods directory");
		if (!modsDir.exists() || !modsDir.isDirectory()) {
			ProgressLogger.progress(2, 3, "Creating Mods Directory");
			modsDir.mkdir();
		}
		ProgressLogger.progress(3, 3, "Mods Directory Located");
	}

	public static ArrayList<ModCandidate> discoverMods() {
		ArrayList<ModCandidate> candidates = new ArrayList<ModCandidate>();
		File[] files = modsDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File modFile = files[i];
			Matcher modMatch = modExt.matcher(modFile.getName());
			ProgressLogger.step(i + 1, files.length, modFile.getName());
			if (!modMatch.find()) {
				System.out.printf("Found non-mod file in mods directory: %s\n", modFile.getName());
				continue;
			}
			if (modMatch.group(1).toLowerCase().equals("jar")) {
				try {
					JarFile modJar = new JarFile(modFile);

					URL[] urls = { new URL("jar:file:" + modFile.getPath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					ArrayList<JarEntry> entries = getEntries(modJar.entries());
					int n = 1;
					for (JarEntry entry : entries) {
						String className = entry.getName();
						className = className.substring(0, className.lastIndexOf('.')).replace('/', '.');
						ProgressLogger.progress(n, entries.size(), String.format("Entry: %s", className));
						candidates.add(new ModCandidate(cl.loadClass(className)));
						n++;
					}
					modJar.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.printf("Unable to load JarFile: %s\n", modFile.getName());
				}
			}

		}
		return candidates;
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
