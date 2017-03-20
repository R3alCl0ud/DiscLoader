package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.render.panel.LoadingPanel;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.start.Main;
import io.discloader.discloader.util.DLUtil;

public class ModRegistry {

	/**
	 * The mod currently being loaded in any given phase of the
	 * {@link DiscLoader loader's} startup
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public static ModContainer activeMod = null;

	public static DiscLoader loader;

	/**
	 * A {@link HashMap} of the mods loaded by the client. Indexed by
	 * {@link Mod#modid()}
	 * 
	 * @author Zachary Waldron
	 * @since 0.0.1
	 */
	public static final HashMap<String, ModContainer> mods = new HashMap<String, ModContainer>();

	/**
	 * Uninitialized mods
	 * 
	 * @author Perry Berman
	 */
	private static final HashMap<String, ModContainer> preInitMods = new HashMap<String, ModContainer>();

	/**
	 * Contains a sensible method of figuring out what mods loaded what mod
	 * 
	 * @author Perry Berman
	 */
	private static final HashMap<String, String> loadMod = new HashMap<String, String>();

	public static void checkCandidates(ArrayList<ModCandidate> mcs) {
		ProgressLogger.step(1, 2, "Checking candidates for @Mod annotation");
		ArrayList<ModContainer> containers = new ArrayList<ModContainer>();
		for (int i = 0; i < mcs.size(); i++) {
			activeMod = null;
			ModCandidate candidate = mcs.get(i);
			Class<?> cls = candidate.getModClass();
			ProgressLogger.progress(i + 1, mcs.size(), cls.getName());
			boolean isMod = cls.isAnnotationPresent(Mod.class);
			if (isMod) {
				ProgressLogger.progress(i + 1, mcs.size(), String.format("Found @Mod Annotation: %s", cls.getName()));
				ModContainer mc = new ModContainer(candidate);
				activeMod = mc;
				containers.add(mc);
			}
		}
		ProgressLogger.step(2, 2, "Registering uninitialized mods");
		for (int i = 0; i < containers.size(); i++) {
			ModContainer mc = containers.get(i);
			activeMod = mc;
			ProgressLogger.progress(i + 1, containers.size(), mc.modInfo.modid());
			if (preInitMods.containsKey(mc.modInfo.modid())) {
				Main.getLogger().severe(String.format(
						"Mod with duplicate id found. \nHALTING STARTUP\nDuplicate ID: %s\n", mc.modInfo.modid()));
				System.exit(1);
			}
			preInitMods.put(mc.modInfo.modid(), mc);
		}
		activeMod = null;
		ProgressLogger.stage(3, 3, "Registering Mod EventHandlers");
		int n = 1;
		for (ModContainer mc : preInitMods.values()) {
			activeMod = mc;
			ProgressLogger.step(n, preInitMods.size(), mc.modInfo.modid());
			mc.discoverHandlers();
			n++;
		}

		activeMod = null;
		ProgressLogger.phase(2, 3, "PreINIT");
		ProgressLogger.stage(1, 3, "Begin PreInit");
		Command.registerCommands();
		ProgressLogger.stage(2, 3, "Registering Default Language");
		LanguageRegistry.registerLanguage(DLUtil.enUS);

		ProgressLogger.stage(3, 3, "Execute PreInit");
		preInit();
		DLPreInitEvent event = new DLPreInitEvent(loader);
		for (IEventListener e : DiscLoader.handlers.values()) {
			e.PreInit(event);
		}
	}

	public static void preInit() {
		int i = 1;
		for (ModContainer mod : preInitMods.values()) {
			ProgressLogger.step(i, preInitMods.size(), String.format("Mod: %s", mod.modInfo.modid()));
			if (mods.containsKey(mod.modInfo.modid())) {
				i++;
				continue;
			}
			load(mod.modInfo.modid());
			// activeMod = null;
		}
		ProgressLogger.phase(3, 3, "Init");
		ProgressLogger.stage(1, 3, "Waiting to Login");
		resetStep();
	}

	public static void load(String modid) {
		ModContainer mod = preInitMods.get(modid);
		ProgressLogger.progress(1, 3, "Checking if another mod is currently active");
		if (activeMod != null) {
			loadMod.put(mod.modInfo.modid(), activeMod.modInfo.modid());
		}
		ProgressLogger.progress(2, 3, "Setting active mod");
		activeMod = mod;

		ProgressLogger.progress(3, 3, "Executing PreInit handler in: " + mod.modInfo.modid());
		mods.put(mod.modInfo.modid(), mod);
		DLPreInitEvent event = new DLPreInitEvent(loader);
		mod.emit("preInit", event);
		if (loadMod.containsKey(mod.modInfo.modid())) {
			activeMod = preInitMods.get(loadMod.get(mod.modInfo.modid()));
		} else {
			activeMod = null;
		}
		mod.loaded = true;
	}

	private static void resetStep() {
		if (!Main.usegui)
			return;
		LoadingPanel.setProgress(0, 0, "");
		LoadingPanel.setStep(0, 0, "");
	}
}