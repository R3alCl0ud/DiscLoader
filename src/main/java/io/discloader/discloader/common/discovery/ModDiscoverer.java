package io.discloader.discloader.common.discovery;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;

/**
 * 
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class ModDiscoverer {

	public static final File modsDir = new File("./mods");
	
	public static void checkModDir() {
		if (!modsDir.exists() || !modsDir.isDirectory()) {
			modsDir.mkdir();
		}
	}
	
	public static void discoverMods() {
		ArrayList<ModCandidate> candidates = new ArrayList<ModCandidate>();
		File[] files = modsDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File modFile = files[i];
			
		}
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
