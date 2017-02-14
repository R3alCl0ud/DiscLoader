package io.disc.discloader.objects.loader;

import java.util.ArrayList;
import java.util.HashMap;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.events.DiscPreInitEvent;

public class ModHandler {

	public final DiscLoader loader;
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