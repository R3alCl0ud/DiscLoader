package io.discloader.discloader.common.discovery;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.discloader.discloader.client.render.ResourceManager;
import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.discovery.Mod.Instance;
import io.discloader.discloader.common.logger.ProgressLogger;

/**
 * @author Perry Berman
 *
 */
public class ModContainer {

	protected static Locale getLocale(String s) {
		s = s.substring(s.lastIndexOf('/') + 1, s.indexOf('.'));
		s = s.replace('_', '-');
		if (s.equals("en-US"))
			return Locale.US;
		else if (s.equals("en-UK"))
			return Locale.UK;
		return Locale.US;
	}

	protected static ArrayList<ZipEntry> readEntries(Enumeration<? extends ZipEntry> enumeration) {
		ArrayList<ZipEntry> entries = new ArrayList<ZipEntry>();
		ZipEntry entry = null;
		while (enumeration.hasMoreElements()) {
			entry = enumeration.nextElement();
			if (entry.isDirectory() || entry.getName().endsWith(".class")) {
				continue;
			}
			entries.add(entry);
		}

		return entries;
	}

	private final ModCandidate mod;

	public final Mod modInfo;

	public boolean loaded;

	public HashMap<String, Method> handlers;

	public Map<String, List<Method>> eHandles;

	protected Object instance;

	private Class<?> modCls;

	public ModContainer(ModCandidate mod) {

		this.mod = mod;

		this.modCls = this.mod.getModClass();

		this.modInfo = this.modCls.getAnnotation(Mod.class);

		this.loaded = false;

		this.handlers = new HashMap<>();
		this.eHandles = new HashMap<>();

		parseAnnotatedFields();
		handleAssets();
	}

	public void discoverHandlers() {
		Method[] methods = this.mod.getModClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			ProgressLogger.progress(i + 1, methods.length, String.format("Method: %s", method.getName()));
			if (method.isAnnotationPresent(Mod.EventHandler.class)) {
				Class<?>[] clzs = method.getParameterTypes();
				if (clzs.length > 1 || clzs.length == 0) {
					continue;
				}
				String paramName = clzs[0].getSimpleName();
				if (!this.eHandles.containsKey(paramName)) {
					this.eHandles.put(paramName, new ArrayList<>());
				}
				this.eHandles.get(paramName).add(method);
			}
		}
	}

	public void emit(Object object) {
		Class<?> clz = object.getClass();
		List<Method> handles = this.eHandles.get(clz.getSimpleName());
		if (handles != null) {
			for (int i = handles.size() - 1; i >= 0; i--) {
				try {
					handles.get(i).invoke(instance, object);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
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

	/**
	 * @return the modCandidate
	 */
	public ModCandidate getMod() {
		return mod;
	}

	private void handleAssets() {
		ZipFile zip = null;
		try {
			try {
				zip = mod.getZipFile();
			} catch (ZipException e) {
				e.printStackTrace();
			}
			if (zip != null) {
				for (ZipEntry e : readEntries(zip.entries())) {
					if (!e.getName().startsWith("assets/" + modInfo.modid()))
						continue;
					String name = e.getName().substring(("assets/" + modInfo.modid()).length() + 1);
					Resource r = new Resource(modInfo.modid(), name);
					ResourceManager.instance.addResource(r);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
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

	private ArrayList<String> parseFields() {
		ArrayList<String> fields = new ArrayList<String>();
		for (Field f : this.modCls.getFields()) {
			fields.add(f.getName());
		}
		return fields;
	}

}
