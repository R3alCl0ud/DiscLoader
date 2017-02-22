package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.render.panel.LoadingPanel;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.events.DiscPreInitEvent;
import io.discloader.discloader.common.start.Main;

public class ModRegistry {

	/**
	 * The mod currently being loaded in any given phase of the
	 * {@link DiscLoader loader's} startup
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public static ModContainer activeMod = null;

	/**
	 * A {@link HashMap} of the mods loaded by the client. Indexed by
	 * {@link Mod#modid()}
	 * 
	 * @author Zachary Waldron
	 * @since 0.0.1
	 */
	public static final HashMap<String, ModContainer> mods = new HashMap<String, ModContainer>();

	/**
	 * 
	 */
	private static final HashMap<String, ModContainer> preInitMods = new HashMap<String, ModContainer>();

	/**
	 * 
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
				ProgressLogger.progress(i + 1, mcs.size(), String.format("Found @Mod Annotation: ", cls.getName()));
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
				System.out.printf("Mod with duplicate id found. \nHALTING STARTUP\nDuplicate ID: %s\n",
						mc.modInfo.modid());
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
//		ProgressLogger.stage(2, 3, "Registering DiscLoader Commands");
		Command.registerCommands();
		ProgressLogger.progress(0, 0, "");

		TimerTask pre = new TimerTask() {
			@Override
			public void run() {
				ProgressLogger.stage(2, 2, "Execute PreInit");
				preInit();
			}
		};
		new Timer().schedule(pre, 750);
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
			activeMod = null;
		}
		
		ProgressLogger.phase(3, 3, "Init");
		ProgressLogger.stage(1, 3, "Logging In");
		resetStep();
		Main.loader.login(Main.token).thenAcceptAsync(action -> {
			ProgressLogger.stage(2, 3, "Caching API Objects");
		});
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
		mod.emit("preInit", new DiscPreInitEvent(Main.loader));
		if (loadMod.containsKey(mod.modInfo.modid())) {
			activeMod = preInitMods.get(loadMod.get(mod.modInfo.modid()));
		} else {
			activeMod = null;
		}
		mod.loaded = true;
	}

	private static void resetStep() {
		if (Main.nogui)
			return;
		LoadingPanel.setProgress(0, 0, "");
		LoadingPanel.setStep(0, 0, "");
	}
}