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

	private final Object instance;

	/**
	 * The default constructor for Mod Candidates
	 * @param modClass The candidate's class file
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ModCandidate(Class<?> modClass) throws InstantiationException, IllegalAccessException {
		this.modClass = modClass;

		this.instance = this.modClass.newInstance();
	}

	/**
	 * @return the instance
	 */
	public Object getInstance() {
		return instance;
	}

	/**
	 * @return the modClass
	 */
	public Class<?> getModClass() {
		return modClass;
	}

}
