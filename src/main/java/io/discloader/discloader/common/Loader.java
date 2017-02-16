package io.discloader.discloader.common;

/**
 * @author Perry Berman
 *
 */
public class Loader {

	private static Loader instance;
	
	private Loader() {
		
	}
	
	public static Loader instance() {
		if (instance == null) {
			instance = new Loader();
		}
		return instance;
	}

}
