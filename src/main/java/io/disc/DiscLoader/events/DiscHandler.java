package io.disc.DiscLoader.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;

public class DiscHandler {
	
	public HashMap<String, ArrayList<Method>> events;
	public DiscLoader loader;
	public DiscHandler(DiscLoader loader) {
		this.loader = loader;
	}
	
	public void loadEvents() {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		Class<?> loaded;
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		for (int i = 0; i < traces.length; i++) {
			if (traces[i].getClassName() != this.getClass().getName()) {
				try {
					loaded = loader.loadClass(traces[i].getClassName());
					Method methods[] = loaded.getDeclaredMethods();
					for (int n = 0; n < methods.length; n++) {
						eventHandler event = methods[n].getAnnotation(io.disc.DiscLoader.events.eventHandler.class);
						if (event != null) {
							System.out.println("found an event handler");
							System.out.println(methods[n].getName());
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
