package io.discloader.discloader.objects.mods;

import java.util.ArrayList;
import java.util.HashMap;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.events.DiscPreInitEvent;
import io.discloader.discloader.objects.annotations.Mod;
import io.discloader.discloader.objects.loader.DiscRegistry;
import io.discloader.discloader.objects.loader.ServiceLoader;

public class ModHandler {

	public final DiscLoader loader;
	
	/**
	 * A {@link HashMap} of the mods loaded by the client. Indexed by {@link Mod#modid()}
	 * @author Zachary Waldron
	 * @since 0.0.1
	 */
	public final HashMap<String, ModContainer> mods;

	public ModHandler(DiscLoader loader) {
		this.loader = loader;
		this.mods = new HashMap<String, ModContainer>();
	}

	public void loadMod(ModContainer mod) {
		if (this.mods.containsKey(mod.ModInfo.modid()))
			return;
		this.mods.put(mod.ModInfo.modid(), mod);
		mod.loadHandlers();
		DiscRegistry.setCurrentActiveMod(mod);
		mod.preInit(new DiscPreInitEvent(this.loader));
	}

	public void beginLoader(String customAnnotationPath) {
		ArrayList<ModContainer> mods = ServiceLoader.loadMods();
		for (ModContainer mod : mods) {
			System.out.println(mod.ModInfo.modid());
			this.loadMod(mod);
		}
	}

	public void beginLoader() {
		this.beginLoader(null);
	}

	public void emit(String event, Object e) {
		for (ModContainer mod : this.mods.values()) {
			mod.ExecuteHandler(event, e);
		}
	}

}