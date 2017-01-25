package io.disc.DiscLoader.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.testClient;

public class DiscHandler {

	public HashMap<String, ArrayList<Method>> events;
	public DiscLoader loader;
	private HashMap<String, Object> instances;

	public DiscHandler(DiscLoader loader) {
		this.loader = loader;
		this.events = new HashMap<String, ArrayList<Method>>();
		this.instances = new HashMap<String, Object>();
		
	}

	public void loadEvents() {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Class<?> loaded;
		StackTraceElement[] traces = Thread.currentThread().getStackTrace();
		for (int i = 0; i < traces.length; i++) {
			if (traces[i].getClassName() != this.getClass().getName()) {
				try {
					loaded = classLoader.loadClass(traces[i].getClassName());
					Method methods[] = loaded.getDeclaredMethods();
					for (int n = 0; n < methods.length; n++) {
						eventHandler event = methods[n].getAnnotation(io.disc.DiscLoader.events.eventHandler.class);
						if (event != null) {
							System.out.println("found an event handler");
							System.out.println(methods[n].getName());
							if (!this.events.containsKey(methods[n].getName())) {
								this.events.put(methods[n].getName(), new ArrayList<Method>());
							}
							this.events.get(methods[n].getName()).add(methods[n]);
							this.instances.put(methods[n].getName(), loaded.newInstance());
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void emit(String event, Object data) {
		if (!this.events.containsKey(event))
			return;
		this.events.get(event).forEach(method -> {
			try {
				Object t = this.instances.get(event);
				method.invoke(t, data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
