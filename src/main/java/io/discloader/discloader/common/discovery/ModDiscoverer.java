package io.discloader.discloader.common.discovery;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.common.logger.ProgressLogger;

/**
 * @author Perry Berman
 * @since 0.0.1
 */
public class ModDiscoverer {

	private static Pattern modExt = Pattern.compile(".(jar|zip)");

	private static Logger logger = DLLogger.getLogger(ModDiscoverer.class);

	public static final File modsDir = new File("./mods");

	public static void checkModDir() {
		ProgressLogger.progress(1, 3, "Looking for mods directory");
		logger.info("Looking for mods directory");
		if (!modsDir.exists() || !modsDir.isDirectory()) {
			ProgressLogger.progress(2, 3, "Creating Mods Directory");
			logger.info("Creating Mods Directory");
			modsDir.mkdir();
		}
		ProgressLogger.progress(3, 3, "Mods Directory Located");
		logger.info("Mods Directory Located");
	}

	public static ArrayList<ModCandidate> discoverMods() {
		ArrayList<ModCandidate> candidates = new ArrayList<ModCandidate>();
		File[] files = modsDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File modFile = files[i];
			Matcher modMatch = modExt.matcher(modFile.getName());
			if (!modMatch.find()) {
				logger.warning(String.format("Found non-mod file in mods directory: %s", modFile.getName()));
				continue;
			}

			if (modMatch.group(1).toLowerCase().equals("jar")) {
				ProgressLogger.step(i + 1, files.length, modFile.getName());
				logger.info(String.format("Reading: %s", modFile.getName()));
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
						logger.fine(String.format("Entry: %s", className));
						candidates.add(new ModCandidate(cl.loadClass(className), modJar));
						n++;
					}
					modJar.close();
				} catch (Exception e) {
					logger.warning(String.format("Unable to load JarFile: %s", modFile.getName()));
					logger.severe(String.format("[%s.%s:%d]: %s", modFile.getName(), e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), e.getLocalizedMessage()));
				}
			}
			if (modMatch.group(1).toLowerCase().equals("zip") || modMatch.group(1).toLowerCase().equals("jar")) {
				ZipReader.readZip(modFile);
			}

		}
		return candidates;
	}

	private static ArrayList<JarEntry> getEntries(Enumeration<JarEntry> jarDir) {
		ArrayList<JarEntry> entries = new ArrayList<JarEntry>();
		while (jarDir.hasMoreElements()) {
			JarEntry entry = jarDir.nextElement();

			if (entry.getName().startsWith("assets")) {
				// TextureRegistry.resourceHandler.addResource(entry);
			}
			if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
				continue;
			}
			entries.add(entry);
		}
		return entries;
	}
}
