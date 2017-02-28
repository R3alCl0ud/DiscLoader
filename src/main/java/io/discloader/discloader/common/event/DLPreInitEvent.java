package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.registry.ModRegistry;

/**
 * this is documentation
 * @author Perry Berman
 *
 */
public class DLPreInitEvent extends DLEvent {
	
	/**
	 * @see ModRegistry#activeMod
	 * @author Perry Berman
	 */
	public final ModContainer activeMod;
	
	/**
	 * Creates a DLPreInitEvent object
	 * @param loader The current instance of DiscLoader
	 * @author Perry Berman
	 */
	public DLPreInitEvent(DiscLoader loader) {
		super(loader);
		this.activeMod = ModRegistry.activeMod;
	}
}
