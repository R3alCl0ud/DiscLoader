package io.disc.discloader.objects.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import io.disc.discloader.events.DiscPreInitEvent;
import io.disc.discloader.objects.annotations.Mod;

/**
 * @author Perry Berman
 *
 */
public class ModContainer {

	public final Class<?> modClass;
	public Object instance;
	public final Mod ModInfo;
	public boolean loaded;
	private HashMap<String, Method> handlers;

	public ModContainer(Class<?> modClass) {
		this.modClass = modClass;

		try {
			this.instance = this.modClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		this.ModInfo = this.modClass.getAnnotation(Mod.class);

		this.loaded = false;
	}

	public void loadHandlers() {
		this.handlers = new HashMap<String, Method>();
		ArrayList<Method> methods = ServiceLoader.loadEventHandlers(this);
		for (Method method : methods) {
			this.handlers.put(method.getName(), method);
			System.out.println(method.getName());
		}
	}

	public void preInit(DiscPreInitEvent e) {
		if (!this.handlers.containsKey("preInit"))
			return;
		try {
			this.handlers.get("preInit").invoke(this.instance, e);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}

	public void ExecuteHandler(String event, Object e) {
		if (!this.handlers.containsKey(event))
			return;
		try {
			this.handlers.get(event).invoke(this.instance, e);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}

}
