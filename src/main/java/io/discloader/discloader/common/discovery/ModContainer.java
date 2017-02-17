package io.discloader.discloader.common.discovery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.discloader.discloader.client.logger.ProgressLogger;

/**
 * @author Perry Berman
 *
 */
public class ModContainer {

	private final ModCandidate mod;

	public final Mod modInfo;

	public boolean loaded;

	public HashMap<String, Method> handlers;

	public ModContainer(ModCandidate mod) {

		this.mod = mod;

		this.modInfo = this.mod.getModClass().getAnnotation(Mod.class);

		this.loaded = false;

		this.handlers = new HashMap<String, Method>();
	}

	/**
	 * @return the modCandidate
	 */
	public ModCandidate getMod() {
		return mod;
	}

	public void discoverHandlers() {
		Method[] methods = this.mod.getModClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			ProgressLogger.progress(i + 1, methods.length, String.format("Method: %s", method.getName()));
			if (method.isAnnotationPresent(Mod.EventHandler.class)) {
				System.out.println(method.getName());
				this.handlers.put(method.getName(), method);
			}
		}
	}

	public void emit(String event, Object object) {
		if (this.handlers.containsKey(event)) {
			try {
				this.handlers.get(event).invoke(this.mod.getInstance(), object);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
