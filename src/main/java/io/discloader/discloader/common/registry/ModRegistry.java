package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.HashMap;

import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.events.DiscPreInitEvent;
import io.discloader.discloader.common.start.Start;

public class ModRegistry {

	public final DiscLoader loader;
	
	public static ModContainer activeMod = null;
	
	/**
	 * A {@link HashMap} of the mods loaded by the client. Indexed by {@link Mod#modid()}
	 * @author Zachary Waldron
	 * @since 0.0.1
	 */
	public final HashMap<String, ModContainer> mods;

	public ModRegistry(DiscLoader loader) {
		this.loader = loader;
		this.mods = new HashMap<String, ModContainer>();
	}

}