package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.registry.ModRegistry;

/**
 * Event emitted when the PreInit phase is executed.
 * 
 * @author Perry Berman
 *
 */
public class DLPreInitEvent extends DLEvent {

	/**
	 * @see ModRegistry#activeMod
	 *
	 */
	public final ModContainer activeMod;

	/**
	 * Creates a DLPreInitEvent object
	 * 
	 * @param loader
	 *            The current instance of DiscLoader
	 * 
	 */
	public DLPreInitEvent(DiscLoader loader) {
		super(loader);
		this.activeMod = ModRegistry.activeMod;
	}
}
