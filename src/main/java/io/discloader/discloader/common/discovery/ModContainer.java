package io.discloader.discloader.common.discovery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.events.DiscPreInitEvent;
import io.discloader.discloader.common.registry.ServiceLoader;

/**
 * @author Perry Berman
 *
 */
public class ModContainer {

	private final ModCandidate modCandidate;
	
	public final Mod ModInfo;
	
	public boolean loaded;

	public ModContainer(ModCandidate mod) {

		this.modCandidate = mod;
		
		this.ModInfo = this.modCandidate.getModClass().getAnnotation(Mod.class);

		this.loaded = false;
	}
	
	/**
	 * @return the modCandidate
	 */
	public ModCandidate getModCandidate() {
		return modCandidate;
	}

}
