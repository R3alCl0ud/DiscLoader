package io.discloader.discloader.common.discovery;

/**
 * contains the instance of a potential mod
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @see Mod
 */
public class ModCandidate {

	private final Class<?> modClass;

	/**
	 * The default constructor for Mod Candidates
	 * @param modClass The candidate's class file
	 */
	public ModCandidate(Class<?> modClass) {
		this.modClass = modClass;
	}

	/**
	 * @return the modClass
	 */
	public Class<?> getModClass() {
		return modClass;
	}

}
