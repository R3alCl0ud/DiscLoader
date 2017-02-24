package io.discloader.discloader.common.discovery;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.common.discovery.Mod.Instance;

/**
 * @author Perry Berman
 *
 */
public class ModContainer {

	private final ModCandidate mod;

	public final Mod modInfo;

	public boolean loaded;

	public HashMap<String, Method> handlers;

	protected Object instance;

	private Class<?> modCls;

	public ModContainer(ModCandidate mod) {

		this.mod = mod;

		this.modCls = this.mod.getModClass();

		this.modInfo = this.modCls.getAnnotation(Mod.class);

		this.loaded = false;

		this.handlers = new HashMap<String, Method>();

		this.parseAnnotatedFields();
	}

	/**
	 * @return the modCandidate
	 */
	public ModCandidate getMod() {
		return mod;
	}

	private ArrayList<String> parseFields() {
		ArrayList<String> fields = new ArrayList<String>();
		for (Field f : this.modCls.getFields()) {
			fields.add(f.getName());
		}
		return fields;
	}

	private void parseAnnotatedFields() {
		try {
			this.instance = modCls.newInstance();
			for (String target : this.parseFields()) {
				Field f = null;
				boolean isStatic = false;
				try {
					f = modCls.getDeclaredField(target);
					f.setAccessible(true);
					isStatic = Modifier.isStatic(f.getModifiers());
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				if (f != null) {
					if (!isStatic) {
						continue;
					}
					if (f.isAnnotationPresent(Instance.class)) {
						f.set(null, this.instance);
					}

				}
			}
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

	}

	public void discoverHandlers() {
		Method[] methods = this.mod.getModClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			ProgressLogger.progress(i + 1, methods.length, String.format("Method: %s", method.getName()));
			if (method.isAnnotationPresent(Mod.EventHandler.class)) {
				this.handlers.put(method.getName(), method);
			}
		}
	}

	public void emit(String event, Object object) {
		if (this.handlers.get(event) != null) {
			try {
				this.handlers.get(event).invoke(this.instance, object);
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
