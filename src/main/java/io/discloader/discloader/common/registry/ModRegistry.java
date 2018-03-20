package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.discovery.ModDiscoverer;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.start.Main;
import io.discloader.discloader.util.DLUtil;

public class ModRegistry {

	/**
	 * The mod currently being loaded in any given phase of the {@link DiscLoader
	 * loader's} startup
	 * 
	 * 
	 * @since 0.0.1
	 */
	public static ModContainer activeMod = null;

	public static DiscLoader loader;

	/**
	 * A {@link HashMap} of the mods loaded by the client. Indexed by
	 * {@link Mod#modid()}
	 * 
	 * 
	 * @since 0.0.1
	 */
	public static final HashMap<String, ModContainer> mods = new HashMap<String, ModContainer>();

	/**
	 * Uninitialized mods
	 * 
	 * 
	 */
	private static final HashMap<String, ModContainer> preInitMods = new HashMap<String, ModContainer>();

	/**
	 * Contains a sensible method of figuring out what mods loaded what mod
	 * 
	 * 
	 */
	private static final Map<String, String> loadMod = new HashMap<String, String>();

	private static final Logger logger = DLLogger.getLogger(ModRegistry.class);

	public static final CompletableFuture<Void> loaded = new CompletableFuture<>();

	public static CompletableFuture<Void> checkCandidates(List<ModCandidate> mcs) {
		Thread modLoader = new Thread("ModLoader") {

			public void run() {
				ProgressLogger.step(1, 2, "Checking candidates for @Mod annotation");
				logger.info("Checking candidates for @Mod annotation");
				List<ModContainer> containers = new ArrayList<ModContainer>();
				for (int i = 0; i < mcs.size(); i++) {
					activeMod = null;
					ModCandidate candidate = mcs.get(i);
					Class<?> cls = candidate.getModClass();
					ProgressLogger.progress(i + 1, mcs.size(), cls.getName());
					logger.info(cls.getName());
					boolean isMod = cls.isAnnotationPresent(Mod.class);
					if (isMod) {
						ProgressLogger.progress(i + 1, mcs.size(), String.format("Found @Mod Annotation: %s", cls.getName()));
						logger.info(String.format("Found @Mod Annotation: %s", cls.getName()));
						ModContainer mc = new ModContainer(candidate);
						activeMod = mc;
						containers.add(mc);
					}
				}
				ProgressLogger.step(2, 2, "Registering uninitialized mods");
				logger.info("Registering uninitialized mods");
				for (int i = 0; i < containers.size(); i++) {
					ModContainer mc = containers.get(i);
					activeMod = mc;
					ProgressLogger.progress(i + 1, containers.size(), mc.modInfo.modid());
					logger.info(mc.modInfo.modid());
					if (preInitMods.containsKey(mc.modInfo.modid())) {
						logger.severe(String.format("Mod with duplicate id found. \nHALTING STARTUP\nDuplicate ID: %s\n", mc.modInfo.modid()));
						System.exit(1);
					}
					preInitMods.put(mc.modInfo.modid(), mc);
				}
				activeMod = null;
				ProgressLogger.stage(3, 3, "Registering Mod EventHandlers");
				logger.info("Registering Mod EventHandlers");
				int n = 1;
				for (ModContainer mc : preInitMods.values()) {
					activeMod = mc;
					ProgressLogger.step(n, preInitMods.size(), mc.modInfo.modid());
					logger.info(String.format("Registrying event handlers in: %s", mc.modInfo.modid()));
					mc.discoverHandlers();
					n++;
				}

				activeMod = null;
				ProgressLogger.phase(2, 3, "PreINIT");
				logger.info("PreINIT");
				ProgressLogger.stage(1, 3, "Begin PreINIT");
				logger.info("Begin PreINIT");
				ProgressLogger.stage(2, 3, "Registering Default Language");
				logger.info("Registering Default Language");
				LanguageRegistry.registerLanguage(DLUtil.enUS);
				ProgressLogger.stage(3, 3, "Execute PreINIT");
				logger.info("Execute PreINIT");
				preInit();
			}
		};
		modLoader.start();
		return loaded;
	}

	public static void preInit() {
		int i = 1;
		for (ModContainer mod : preInitMods.values()) {
			ProgressLogger.step(i, preInitMods.size(), String.format("Mod: %s", mod.modInfo.modid()));
			if (mods.containsKey(mod.modInfo.modid())) {
				i++;
				continue;
			}
			loadMod(mod.modInfo.modid());
			// activeMod = null;
		}
		// DLPreInitEvent event = new DLPreInitEvent(loader);
		logger.info("" + (loader != null));
		// loader.emit(event);
		ProgressLogger.phase(3, 3, "Init");
		logger.info("Now swiching to the Init phase");
		ProgressLogger.stage(1, 3, "Waiting to Login");
		logger.info("Waiting to Login");
		// loader.doneLoading();
		resetStep();
		loaded.complete((Void) null);
	}

	public static CompletableFuture<Void> startMods() {
		ProgressLogger.stage(1, 3, "Mod Discovery");
		logger.info("Beginning Mod Discovery");
		ModDiscoverer.checkModDir();
		List<ModCandidate> candidates = ModDiscoverer.discoverMods();
		ProgressLogger.stage(2, 3, "Discovering Mod Containers");
		logger.info("Discovering Mod Containers");
		return checkCandidates(candidates);
	}

	public static void loadMod(String modid) {
		ModContainer mod = preInitMods.get(modid);
		ProgressLogger.progress(1, 3, "Checking if another mod is currently active");
		logger.info("Checking if another mod is currently active");
		if (activeMod != null) {
			loadMod.put(mod.modInfo.modid(), activeMod.modInfo.modid());
		}
		ProgressLogger.progress(2, 3, "Setting active mod");
		logger.info("Setting active mod");
		activeMod = mod;

		ProgressLogger.progress(3, 3, "Executing PreInit handler in: " + mod.modInfo.modid());
		logger.info("Executing PreInit handler in: " + mod.modInfo.modid());
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
		// LoadingPanel.setProgress(0, 0, "");
		// LoadingPanel.setStep(0, 0, "");
	}
}