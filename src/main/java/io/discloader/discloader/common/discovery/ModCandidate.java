package io.discloader.discloader.common.discovery;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

/**
 * contains the instance of a potential mod
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @see Mod
 */
public class ModCandidate {
	
	private final Class<?> modClass;
	private final JarFile jar;
	
	/**
	 * The default constructor for Mod Candidates
	 * 
	 * @param modClass The candidate's class file
	 * @param jar The jar the class is from
	 */
	public ModCandidate(Class<?> modClass, JarFile jar) {
		this.modClass = modClass;
		this.jar = jar;
	}
	
	/**
	 * @return the modClass
	 */
	public Class<?> getModClass() {
		return modClass;
	}
	
	/**
	 * @return the jar
	 * @throws IOException
	 */
	public JarFile getJarFile() throws IOException {
		return new JarFile(jar.getName());
	}
	
	public File getFile() {
		return new File(jar.getName());
	}
	
	public ZipFile getZipFile() throws IOException {
		return new ZipFile(jar.getName());
	}
}
