/**
 * 
 */
package io.disc.discloader.objects.loader;

import java.lang.reflect.Method;
import java.util.ArrayList;

import io.disc.discloader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class ServiceLoader {

	public final DiscLoader loader;
	
	/**
	 * 
	 */
	public ServiceLoader(DiscLoader loader) {
		this.loader = loader;
	}

	public void loadMods() {
		
	}
	
	
	public ArrayList<Method> loadEventHandlers(Class<?> cls) {
		return null;
	}
	
}
