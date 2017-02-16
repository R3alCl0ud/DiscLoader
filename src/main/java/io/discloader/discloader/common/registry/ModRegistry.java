package io.discloader.discloader.common.registry;

import java.util.ArrayList;
import java.util.HashMap;

import io.discloader.discloader.client.logging.ProgressLogger;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModContainer;

public class ModRegistry {
	
	/**
	 * The mod currently being loaded in any given phase of the {@link DiscLoader loader's} startup
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public static ModContainer activeMod = null;
	
	/**
	 * A {@link HashMap} of the mods loaded by the client. Indexed by {@link Mod#modid()}
	 * @author Zachary Waldron
	 * @since 0.0.1
	 */
	public static final HashMap<String, ModContainer> mods = new HashMap<String, ModContainer>();
	
	private static final HashMap<String, ModContainer> preInitMods = new HashMap<String, ModContainer>();
	
	public static void checkCandidates(ArrayList<ModCandidate> mcs) {
		ProgressLogger.step(1, 2, "Checking candidates for @Mod annotation");
		ArrayList<ModContainer> containers = new ArrayList<ModContainer>();
		for (int i = 0; i < mcs.size(); i++) {
			ModCandidate candidate = mcs.get(i);
			Class<?> cls = candidate.getModClass();
			ProgressLogger.progress(i + 1, mcs.size(), cls.getName());
			boolean isMod = cls.isAnnotationPresent(Mod.class);
			if (isMod) {
				ModContainer mc = new ModContainer(candidate);
				containers.add(mc);
			}
		}
	}

}