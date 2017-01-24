package io.disc.DiscLoader.objects.loader;

import java.lang.reflect.Method;
import java.util.Optional;

public class ModHandler {
	private ClassLoader modhandler;
	private Class<?> instance;
	private Method methods[];
	
	// Set annotation class name
	private static String ann_class = "io.disc.DiscLoader.objects.loader.Mod.class";
	
	public void beginLoader(Optional<String> customPath) {
		
	}
}
